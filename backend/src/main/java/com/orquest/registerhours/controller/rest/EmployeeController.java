package com.orquest.registerhours.controller.rest;

import com.orquest.registerhours.controller.resources.EmployeeIdResource;
import com.orquest.registerhours.controller.resources.LoginRequest;
import com.orquest.registerhours.controller.resources.alert.AlertResource;
import com.orquest.registerhours.controller.resources.alert.AlertResourceMapper;
import com.orquest.registerhours.controller.resources.register.RegisterDayResource;
import com.orquest.registerhours.controller.resources.register.RegisterResource;
import com.orquest.registerhours.controller.resources.register.RegisterWeekResource;
import com.orquest.registerhours.service.EmployeeService;
import com.orquest.registerhours.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller that handles request to employee
 */
@RestController
@Slf4j
@RequestMapping( value = "employee", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
public class EmployeeController {

    private final RegisterService registerService;
    private final EmployeeService employeeService;

    public EmployeeController( RegisterService registerService, EmployeeService employeeService ) {
        this.registerService = registerService;
        this.employeeService = employeeService;
    }

    /**
     * Login based on employee number
     * @param request {@link LoginRequest}
     * @return Id of employee
     */
    @PostMapping( value = "/login" )
    public ResponseEntity<EmployeeIdResource> loginWithNumber( @RequestBody LoginRequest request ) {
        log.info("Login request {}", request);
        return ResponseEntity.ok(new EmployeeIdResource(employeeService.doLogin(request)));
    }

    /**
     * Get alert of employee
     *
     * @param id Id of employee
     */
    @GetMapping( value = "/{id}/alerts" )
    public ResponseEntity<List<AlertResource>> getEmployeeAlerts(@PathVariable String id ) {
        log.info("Get alerts of employee id {}", id);
        return ResponseEntity.ok(AlertResourceMapper.INSTANCE.alertsToAlertsResource(employeeService.getEmployeeAlerts(id)));
    }

    /**
     * Get registers of the day
     *
     * @param id Id of the employee
     * @param day Day to get
     * @return List of {@link RegisterResource} with information of the registers
     */
    @GetMapping( value = "/{id}/register/day/{day}" )
    public ResponseEntity<RegisterDayResource> getEmployeeRegisterOfDayWithHours(@PathVariable String id, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day ) {
        log.info("Get register of day {} for employee {}", day, id);
        return ResponseEntity.ok(registerService.getRegistersOfDayOfEmployee(id, day));
    }

    /**
     * Get registers of the week
     *
     * @param id Id of the employee
     * @param week Week of the year
     */
    @GetMapping( value = "/{id}/register/year/{year}/week/{week}")
    public ResponseEntity<RegisterWeekResource> getEmployeeRegisterOfWeekWithHours(@PathVariable String id, @PathVariable Integer year, @PathVariable Integer week ) {
        log.info("Get register of weak {} for employee {}", week, id);
        return ResponseEntity.ok(registerService.getRegistersOfWeekOfEmployee(id, year, week));
    }
}
