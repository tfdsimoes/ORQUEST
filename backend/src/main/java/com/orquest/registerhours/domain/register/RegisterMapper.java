package com.orquest.registerhours.domain.register;

import com.orquest.registerhours.controller.resources.register.RegisterNewRequest;
import com.orquest.registerhours.domain.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.NullValueCheckStrategy.ON_IMPLICIT_CONVERSION;

@Mapper( nullValueCheckStrategy = ON_IMPLICIT_CONVERSION )
public interface RegisterMapper {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    @Mapping( target = "id", ignore = true )
    @Mapping( target = "employee", source = "employee")
    @Mapping( target = "businessId", source = "registerNewRequest.businessId" )
    @Mapping( target = "date", source = "registerNewRequest.date" )
    @Mapping( target = "recordType", expression = "java(RegisterRecordType.valueOf(registerNewRequest.getRecordType().name()))" )
    @Mapping( target = "serviceId", source = "registerNewRequest.serviceId" )
    @Mapping( target = "type", expression = "java(RegisterType.valueOf(registerNewRequest.getType().name()))" )
    Register RegisterNewRequestToRegister( RegisterNewRequest registerNewRequest, Employee employee );
}
