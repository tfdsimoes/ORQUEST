package com.orquest.registerhours.repository;

import com.orquest.registerhours.domain.Employee;
import com.orquest.registerhours.domain.alert.Alert;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;

/**
 * Alert spring data repository
 */
public interface AlertRepository extends PagingAndSortingRepository<Alert, String> {

    void deleteAllByEmployeeAndDate( Employee employee, LocalDate date );

}
