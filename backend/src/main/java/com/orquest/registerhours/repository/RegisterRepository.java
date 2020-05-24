package com.orquest.registerhours.repository;

import com.orquest.registerhours.domain.register.Register;
import com.orquest.registerhours.domain.register.RegisterRecordType;
import com.orquest.registerhours.domain.register.RegisterType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Register spring data repository
 */
public interface RegisterRepository extends PagingAndSortingRepository<Register, String> {

    List<Register> findAllByEmployeeIdAndDateBetweenOrderByDate( String employeeId, ZonedDateTime from, ZonedDateTime to );

    Optional<Register> findFirstByEmployeeIdAndTypeAndRecordTypeAndDateBetweenOrderByDate(String employeeId, RegisterType type, RegisterRecordType recordType, ZonedDateTime from, ZonedDateTime to );
}
