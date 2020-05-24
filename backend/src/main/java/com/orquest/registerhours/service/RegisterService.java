package com.orquest.registerhours.service;

import com.orquest.registerhours.controller.resources.register.RegisterDayResource;
import com.orquest.registerhours.controller.resources.register.RegisterNewRequest;
import com.orquest.registerhours.controller.resources.register.RegisterUpdateRequest;
import com.orquest.registerhours.controller.resources.register.RegisterWeekResource;

import java.time.LocalDate;
import java.util.List;

/**
 * Register service interface
 */
public interface RegisterService {

    /**
     * Process new registers into the system
     *
     * @param request List of {@link RegisterNewRequest} JSON with list of new registers
     * @return List of Strings with the new ids
     */
    List<String> newRegisters( List<RegisterNewRequest> request );

    /**
     * Get registers of the day with total hours
     *
     * @param employeeId Id of employee
     * @param date Date to check
     * @return {@link RegisterDayResource}
     */
    RegisterDayResource getRegistersOfDayOfEmployee(String employeeId, LocalDate date );

    /**
     * Get registers of a timeline
     *
     * @param employeeId Id of employee
     * @param year year to check
     * @param week day of the week of the year to check
     * @return {@link RegisterWeekResource}
     */
    RegisterWeekResource getRegistersOfWeekOfEmployee(String employeeId, Integer year, Integer week );

    /**
     * Delete a register from the system
     *
     * @param registerId Id of register
     */
    void deleteRegister( String registerId );

    /**
     * Update a register in the system
     *
     * @param registerId Id of register to update
     * @param request {@link RegisterUpdateRequest} with the data to update
     * @return the id of the register that updated
     */
    String updateRegister( String registerId, RegisterUpdateRequest request );

}
