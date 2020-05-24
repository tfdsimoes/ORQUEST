package com.orquest.registerhours.controller;

import com.orquest.registerhours.exception.EmployeeNotFound;
import com.orquest.registerhours.exception.NoDataUpdate;
import com.orquest.registerhours.exception.RegisterNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Advice controller to handle exceptions
 */
@Slf4j
@RestControllerAdvice
public class EndpointAdvice {

    @ExceptionHandler( {EmployeeNotFound.class} )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    public String employeeNotFound( EmployeeNotFound ex ) {
        log.error(ex.getMessage(), ex);
        return "Employee id not found";
    }

    @ExceptionHandler( {RegisterNotFound.class} )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    public String registerNotFound( RegisterNotFound ex ) {
        log.error(ex.getMessage(), ex);
        return "Register id not found";
    }

    @ExceptionHandler( {NoDataUpdate.class} )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    public String noDataUpdate( NoDataUpdate ex ) {
        log.error(ex.getMessage(), ex);
        return "No data for update in register";
    }
}
