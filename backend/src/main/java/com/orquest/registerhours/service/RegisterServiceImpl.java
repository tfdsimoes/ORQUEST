package com.orquest.registerhours.service;

import com.orquest.registerhours.RegisterHoursProperties;
import com.orquest.registerhours.controller.resources.register.RegisterDayResource;
import com.orquest.registerhours.controller.resources.register.RegisterNewRequest;
import com.orquest.registerhours.controller.resources.register.RegisterResourceMapper;
import com.orquest.registerhours.controller.resources.register.RegisterUpdateRequest;
import com.orquest.registerhours.controller.resources.register.RegisterWeekResource;
import com.orquest.registerhours.domain.Employee;
import com.orquest.registerhours.domain.alert.Alert;
import com.orquest.registerhours.domain.alert.AlertType;
import com.orquest.registerhours.domain.register.Register;
import com.orquest.registerhours.domain.register.RegisterMapper;
import com.orquest.registerhours.domain.register.RegisterRecordType;
import com.orquest.registerhours.domain.register.RegisterType;
import com.orquest.registerhours.exception.EmployeeNotFound;
import com.orquest.registerhours.exception.NoDataUpdate;
import com.orquest.registerhours.exception.RegisterNotFound;
import com.orquest.registerhours.repository.AlertRepository;
import com.orquest.registerhours.repository.EmployeeRepository;
import com.orquest.registerhours.repository.RegisterRepository;
import com.orquest.registerhours.utils.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {

    private final RegisterRepository registerRepository;

    private final EmployeeRepository employeeRepository;

    private final AlertRepository alertRepository;

    private final RegisterHoursProperties properties;

    public RegisterServiceImpl( RegisterRepository registerRepository, EmployeeRepository employeeRepository, AlertRepository alertRepository,
                                RegisterHoursProperties properties ) {
        this.registerRepository = registerRepository;
        this.employeeRepository = employeeRepository;
        this.alertRepository = alertRepository;
        this.properties = properties;
    }

    /**
     * Process new registers into the system
     *
     * @param request {@link RegisterNewRequest} JSON with list of new registers
     * @return List of Strings with the new ids
     */
    @Override
    @Transactional
    public List<String> newRegisters(List<RegisterNewRequest> request) {
        log.info("Processing new registers {}", request);

        Map<Employee, Set<LocalDate>> employeeDatesInserted = new HashMap<>();
        List<String> addedRegisters = new ArrayList<>();

        log.debug("Adding requests");
        for( RegisterNewRequest registerNewRequest : request ) {
            Employee employee = getEmployeeOrCreateEmployee( registerNewRequest.getEmployeeId() );

            Register newRegister = registerRepository.save(RegisterMapper.INSTANCE.RegisterNewRequestToRegister(registerNewRequest, employee));
            addedRegisters.add(newRegister.getId());

            Set<LocalDate> datesInserted = employeeDatesInserted.get( employee );

            if ( datesInserted != null ) {
                datesInserted.add(newRegister.getDate().toLocalDate());
            } else {
                datesInserted = new HashSet<>();
                datesInserted.add(newRegister.getDate().toLocalDate());
            }

            employeeDatesInserted.put( employee, datesInserted );
        }

        checkForAlerts( employeeDatesInserted );

        return addedRegisters;
    }

    /**
     * Get registers of the day with total hours
     *
     * @param employeeId Id of employee
     * @param date Date to check
     * @return {@link RegisterDayResource}
     */
    @Override
    public RegisterDayResource getRegistersOfDayOfEmployee(String employeeId, LocalDate date ) {
        log.info("Processing get registers of employee {} with hours of {}", employeeId, date);
        Optional<Employee> optionalEmployee = employeeRepository.findById( employeeId );

        if ( !optionalEmployee.isPresent() ) {
            throw new EmployeeNotFound("Id does not exist in the system");
        }

        return buildRegisterDay( employeeId, date );
    }

    /**
     * Get registers of a timeline
     *
     * @param employeeId Id of employee
     * @param year year to check
     * @param week       day of the week of the year to check
     * @return {@link RegisterWeekResource}
     */
    @Override
    public RegisterWeekResource getRegistersOfWeekOfEmployee(String employeeId, Integer year, Integer week ) {
        log.info("Processing registers of week {} of year {} from employee {}", week, year, employeeId);
        Optional<Employee> optionalEmployee = employeeRepository.findById( employeeId );

        if ( !optionalEmployee.isPresent() ) {
            throw new EmployeeNotFound("Id does not exist in the system");
        }

        Calendar cld = Calendar.getInstance(new Locale("en", "UK"));
        cld.set(Calendar.YEAR, year);
        cld.set(Calendar.WEEK_OF_YEAR, week);

        cld.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        LocalDate startWeek = cld.getTime().toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDate();

        cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        LocalDate endWeek = cld.getTime().toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDate().plusDays(7);

        log.debug("Start of the weak {} - End of the weak {}", startWeek, endWeek);

        long totalDays = DAYS.between(startWeek, endWeek);

        List<RegisterDayResource> registerDays = new ArrayList<>();
        long totalHours = 0L;

        for( long i = 0; i <= totalDays; i++ ) {
            RegisterDayResource registerDay = buildRegisterDay( employeeId, startWeek.plusDays(i));
            totalHours += registerDay.getTotalWork();
            registerDays.add(registerDay);
        }

        return RegisterWeekResource.builder().totalWork(totalHours).registersDay(registerDays).build();
    }

    /**
     * Delete a register from the system
     *
     * @param registerId Id of register
     */
    @Override
    @Transactional
    public void deleteRegister(String registerId) {
        log.info("Processing deleting register {}", registerId);

        Optional<Register> optionalRegister = registerRepository.findById(registerId);

        if( !optionalRegister.isPresent() ){
            throw new RegisterNotFound("Register id does not exist");
        }

        Register register = optionalRegister.get();
        registerRepository.deleteById( registerId );

        Map<Employee, Set<LocalDate>> datesChanged = new HashMap<>();
        datesChanged.put(register.getEmployee(), Collections.singleton(register.getDate().toLocalDate()));
        checkForAlerts(datesChanged);
    }

    /**
     * Update a register in the system
     *
     * @param registerId Id of register to update
     * @param request    {@link RegisterUpdateRequest} with the data to update
     * @return the id of the register that updated
     */
    @Override
    @Transactional
    public String updateRegister(String registerId, RegisterUpdateRequest request) {
        log.info("Processing update register {} with {}", registerId, request);

        Optional<Register> optionalRegister = registerRepository.findById( registerId );

        if( !optionalRegister.isPresent() ) {
            throw new RegisterNotFound("Id of register does not exist");
        }

        Register oldRegister = optionalRegister.get();
        boolean changes = false;

        if( request.getDate() != null ) {
            oldRegister.setDate(request.getDate());
            changes = true;
        }

        if( request.getRecordType() != null ) {
            oldRegister.setRecordType(RegisterRecordType.fromString(request.getRecordType().name()));
            changes = true;
        }

        if( request.getType() != null ) {
            oldRegister.setType(RegisterType.fromString(request.getType().name()));
            changes = true;
        }

        if( !changes ) {
            throw new NoDataUpdate("Register update without data");
        }

        Register updateRegister = registerRepository.save(oldRegister);
        Map<Employee, Set<LocalDate>> datesChanged = new HashMap<>();
        datesChanged.put( updateRegister.getEmployee(), Collections.singleton(updateRegister.getDate().toLocalDate()) );
        checkForAlerts(datesChanged);

        return updateRegister.getId();
    }

    private RegisterDayResource buildRegisterDay(String employeeId, LocalDate date ) {
        List<Register> registers = registerRepository.findAllByEmployeeIdAndDateBetweenOrderByDate(
                employeeId,
                ZonedDateTime.of( date, LocalTime.MIDNIGHT, TimeZone.getDefault().toZoneId() ),
                ZonedDateTime.of( date, LocalTime.MAX, TimeZone.getDefault().toZoneId() )
        );

        return RegisterDayResource.builder()
                .totalWork(calculateHours( employeeId, date ))
                .date( date )
                .registers( RegisterResourceMapper.INSTANCE.registersToRegistersResource(registers) )
                .build();
    }

    /**
     * Check the days of employee to see if there is the need to raise a new alert
     *
     * @param employeeDatesInserted Map with key employee and value of list of localDate
     */
    private void checkForAlerts( Map<Employee, Set<LocalDate>> employeeDatesInserted ) {
        log.info("Checking for new alerts");
        employeeDatesInserted.forEach(
                (employee, listDates) -> {
                    List<Alert> alerts = new ArrayList<>();
                    for( LocalDate date : listDates ) {

                        alertRepository.deleteAllByEmployeeAndDate( employee, date );

                        if( checkRegistersDayIsOk( employee.getId(), date) ) {
                            alerts.add(
                                    Alert.builder()
                                            .date(date)
                                            .employee(employee)
                                            .type(AlertType.INCOMPLETE)
                                            .build()
                            );
                        }

                        if( calculateHours( employee.getId(), date) > properties.getMaxHoursDay() ) {
                            alerts.add(
                                    Alert.builder()
                                            .date(date)
                                            .employee(employee)
                                            .type(AlertType.MAX_HOURS)
                                            .build()
                            );
                        }

                        if( checkStartWorkTime( employee.getId(), date) ) {
                            alerts.add(
                                    Alert.builder()
                                            .date(date)
                                            .employee(employee)
                                            .type(AlertType.START_BEFORE_TIME)
                                            .build()
                            );
                        }

                    }

                    alertRepository.saveAll(alerts);
                }
        );
    }

    /**
     * Calculated the time worked in that day
     *
     * @param employeeId employee id to calculate
     * @param date day to calculate the hours
     * @return return the amount of time in milliseconds
     */
    private long calculateHours( String employeeId, LocalDate date ) {
        log.info("Calculating hours worked of {} on {}", employeeId, date);
        long totalWorkTime = 0L;
        long totalRestTime = 0L;

        List<Register> registersOfDay = registerRepository.findAllByEmployeeIdAndDateBetweenOrderByDate(
                employeeId,
                ZonedDateTime.of( date, LocalTime.MIDNIGHT, TimeZone.getDefault().toZoneId() ),
                ZonedDateTime.of( date, LocalTime.MAX, TimeZone.getDefault().toZoneId() )
        );

        ZonedDateTime startWorkTime = null;
        ZonedDateTime startRestTime = null;

        // Check the work done in the day
        log.debug("Checking the day");
        for( Register register : registersOfDay ) {
            if ( register.getRecordType().equals(RegisterRecordType.IN) && register.getType().equals(RegisterType.WORK) ) {
                startWorkTime = register.getDate();
            } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.WORK) && startWorkTime != null ) {
                totalWorkTime = totalWorkTime + ChronoUnit.MILLIS.between(startWorkTime, register.getDate());
                startWorkTime = null;
                startRestTime = null;
            } else if ( register.getRecordType().equals(RegisterRecordType.IN) && register.getType().equals(RegisterType.REST) && startWorkTime != null ) {
                startRestTime = register.getDate();
            } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.REST) && startRestTime != null ) {
                totalRestTime = totalRestTime + ChronoUnit.MILLIS.between(startRestTime, register.getDate());
                startRestTime = null;
            }
        }

        // Started in the day and ended in the next day
        log.debug("Overnight shift, checking next day");
        if( startWorkTime != null ){
            List<Register> registersNextDay = registerRepository.findAllByEmployeeIdAndDateBetweenOrderByDate(
                    employeeId,
                    ZonedDateTime.of( date.plusDays(1L), LocalTime.MIDNIGHT, TimeZone.getDefault().toZoneId() ),
                    ZonedDateTime.of( date.plusDays(1L), LocalTime.MAX, TimeZone.getDefault().toZoneId() )
            );

            for( Register register : registersNextDay ) {
                if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.WORK) ) {
                    totalWorkTime = totalWorkTime + ChronoUnit.MILLIS.between(startWorkTime, register.getDate());
                    return totalWorkTime - totalRestTime;
                } else if ( register.getRecordType().equals(RegisterRecordType.IN) && register.getType().equals(RegisterType.REST) ) {
                    startRestTime = register.getDate();
                } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.REST) && startRestTime != null ) {
                    totalRestTime = totalRestTime + ChronoUnit.MILLIS.between(startRestTime, register.getDate());
                    startRestTime = null;
                }
            }
        }

        return totalWorkTime - totalRestTime;
    }

    /**
     * Check if the register of work is complete
     *
     * @param employeeId Employee id
     * @param date Date to check
     * @return boolean with the result
     */
    private boolean checkRegistersDayIsOk( String employeeId, LocalDate date ) {
        log.info("Check if registers of {} on {} are ok", employeeId, date);
        List<Register> registersOfDay = registerRepository.findAllByEmployeeIdAndDateBetweenOrderByDate(
                employeeId,
                ZonedDateTime.of( date, LocalTime.MIDNIGHT, TimeZone.getDefault().toZoneId() ),
                ZonedDateTime.of( date, LocalTime.MAX, TimeZone.getDefault().toZoneId() )
        );

        boolean startWork = false;
        boolean startRest = false;
        boolean firstRegister = true;

        // Check the work done in the day
        for( Register register : registersOfDay ) {
            log.info("Checking the day");
            if ( register.getRecordType().equals(RegisterRecordType.IN) && register.getType().equals(RegisterType.WORK) && !startWork) {
                startWork = true;
            } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.WORK)
                    && firstRegister  && checkIfOvernightShift( employeeId, date.minusDays(1L) )) {
                // Is a overnight shift nothing to do ... but is a case to catch
            } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.WORK) && startWork ) {
                startWork = false;
            } else if ( register.getRecordType().equals(RegisterRecordType.IN) && register.getType().equals(RegisterType.REST) && startWork && !startRest ) {
                startRest = true;
            } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.REST) && startWork && startRest ) {
                startRest = false;
            } else {
                return true;
            }
            firstRegister = false;
        }

        // Started in the day and ended in the next day
        if( startWork ) {
            log.info("Overnight shift, checking next day");
            List<Register> registersNextDay = registerRepository.findAllByEmployeeIdAndDateBetweenOrderByDate(
                    employeeId,
                    ZonedDateTime.of( date.plusDays(1L), LocalTime.MIDNIGHT, TimeZone.getDefault().toZoneId() ),
                    ZonedDateTime.of( date.plusDays(1L), LocalTime.MAX, TimeZone.getDefault().toZoneId() )
            );

            for( Register register : registersNextDay ) {
                if ( register.getRecordType().equals(RegisterRecordType.IN) && register.getType().equals(RegisterType.WORK) ) {
                    return true;
                } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.WORK) ) {
                    return false;
                } else if ( register.getRecordType().equals(RegisterRecordType.IN) && register.getType().equals(RegisterType.REST) && !startRest ) {
                    startRest = true;
                } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.REST) && startRest ) {
                    startRest = false;
                } else {
                    return true;
                }
            }
        }

        return startRest || startWork;
    }

    /**
     * Function to check if start working in previous day
     *
     * @param employeeId Id of the employee
     * @param date Date to check
     * @return true or false if stated in previous day
     */
    private boolean checkIfOvernightShift( String employeeId, LocalDate date ) {
        log.info("Check if  {} on {} made overnight shift", employeeId, date);
        List<Register> registersOfDay = registerRepository.findAllByEmployeeIdAndDateBetweenOrderByDate(
                employeeId,
                ZonedDateTime.of( date, LocalTime.MIDNIGHT, TimeZone.getDefault().toZoneId() ),
                ZonedDateTime.of( date, LocalTime.MAX, TimeZone.getDefault().toZoneId() )
        );

        boolean startWork = false;

        for( Register register : registersOfDay ) {
            if ( register.getRecordType().equals(RegisterRecordType.IN) && register.getType().equals(RegisterType.WORK) && !startWork) {
                startWork = true;
            } else if ( register.getRecordType().equals(RegisterRecordType.OUT) && register.getType().equals(RegisterType.WORK) && startWork ) {
                startWork = false;
            }
        }

        return startWork;
    }

    /**
     * Check if the employee start the day before min time
     *
     * @param employeeId employee id
     * @param date date to check
     * @return true or false if employee start after the min hour
     */
    private boolean checkStartWorkTime( String employeeId , LocalDate date ){
        log.info("Checking if employee {} on {} started working after the min time", employeeId, date);
        Optional<Register> optionalRegister = registerRepository.findFirstByEmployeeIdAndTypeAndRecordTypeAndDateBetweenOrderByDate(
                employeeId,
                RegisterType.WORK,
                RegisterRecordType.IN,
                ZonedDateTime.of( date, LocalTime.MIDNIGHT, TimeZone.getDefault().toZoneId() ),
                ZonedDateTime.of( date, LocalTime.MAX, TimeZone.getDefault().toZoneId() )
        );

        if( !optionalRegister.isPresent() ) {
            return false;
        }

        Register register = optionalRegister.get();
        long dayRegisterStartMilli = Converter.converterTimeToMilli(register.getDate());

        switch (register.getDate().getDayOfWeek()) {
            case MONDAY:
                return properties.getMinMonday() > dayRegisterStartMilli;
            case TUESDAY:
                return properties.getMinTuesday() > dayRegisterStartMilli;
            case WEDNESDAY:
                return properties.getMinWednesday() > dayRegisterStartMilli;
            case THURSDAY:
                return properties.getMinThursday() > dayRegisterStartMilli;
            case FRIDAY:
                return properties.getMinFriday() > dayRegisterStartMilli;
            case SATURDAY:
                return properties.getMinSaturday() > dayRegisterStartMilli;
            case SUNDAY:
                return properties.getMinSunday() > dayRegisterStartMilli;
            default:
                throw new RuntimeException("Break in days of week");
        }
    }

    /**
     * Function to get or create new employee (since there is no database of employees)
     *
     * @param employeeNumber Employee number to search
     * @return {@link Employee} Employee entity object
     */
    private Employee getEmployeeOrCreateEmployee( String employeeNumber ) {
        log.info("Getting or creating new user in the system with number {}", employeeNumber);
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByNumber( employeeNumber );

        if( optionalEmployee.isPresent() ) {
            return optionalEmployee.get();
        }

        Employee newEmployee = Employee.builder()
                .number(employeeNumber)
                .build();

        return employeeRepository.save(newEmployee);
    }
}
