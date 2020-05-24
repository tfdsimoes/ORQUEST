package com.orquest.registerhours.service;

import com.orquest.registerhours.controller.resources.LoginRequest;
import com.orquest.registerhours.domain.Employee;
import com.orquest.registerhours.domain.alert.Alert;
import com.orquest.registerhours.exception.EmployeeNotFound;
import com.orquest.registerhours.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    public final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl( EmployeeRepository employeeRepository ) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Function that will collect all the alerts of a employee
     *
     * @param employeeId Id of the employee
     * @return List of {@link Alert}
     */
    @Override
    public List<Alert> getEmployeeAlerts( String employeeId ) {
        log.info("Processing get employee {} alerts", employeeId);

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if( !employeeOptional.isPresent() ) {
            throw new EmployeeNotFound("Id does not exist in the system");
        }

        return employeeOptional.get().getAlerts();
    }

    /**
     * Do login into the system
     *
     * @param request {@link LoginRequest}
     * @return Id of employee
     */
    @Override
    public String doLogin(LoginRequest request) {
        log.info("Doing login {}", request);
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByNumber( request.getNumber() );

        if( optionalEmployee.isPresent() ) {
            return optionalEmployee.get().getId();
        }

        throw new EmployeeNotFound("Employee not found");
    }
}
