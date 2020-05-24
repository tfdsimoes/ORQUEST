package com.orquest.registerhours.repository;

import com.orquest.registerhours.domain.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Employee spring data repository
 */
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, String> {

    Optional<Employee> findEmployeeByNumber( String number );

}
