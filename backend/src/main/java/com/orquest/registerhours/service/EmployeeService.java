package com.orquest.registerhours.service;

import com.orquest.registerhours.controller.resources.LoginRequest;
import com.orquest.registerhours.domain.alert.Alert;

import java.util.List;

/**
 * Employee service interface
 */
public interface EmployeeService {

    /**
     * Function that will collect all the alerts of a employee
     *
     * @param employeeId Id of the employee
     * @return List of {@link Alert}
     */
    List<Alert> getEmployeeAlerts( String employeeId );

    /**
     * Do login into the system
     *
     * @param request {@link LoginRequest}
     * @return Id of employee
     */
    String doLogin( LoginRequest request );
}
