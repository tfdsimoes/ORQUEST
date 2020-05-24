package com.orquest.registerhours.controller.resources.register;

import com.orquest.registerhours.domain.register.Register;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ON_IMPLICIT_CONVERSION;

@Mapper( nullValueCheckStrategy = ON_IMPLICIT_CONVERSION )
public interface RegisterResourceMapper {

    RegisterResourceMapper INSTANCE = Mappers.getMapper( RegisterResourceMapper.class );

    @Mapping( target = "id", source = "id" )
    @Mapping( target = "date", source = "date" )
    @Mapping( target = "recordType", expression = "java(RegisterRecordType.valueOf(register.getRecordType().name()))" )
    @Mapping( target = "type", expression = "java(RegisterType.valueOf(register.getType().name()))" )
    RegisterResource RegisterToRegisterResource( Register register );

    @IterableMapping( qualifiedByName = "reduced" )
    List<RegisterResource> registersToRegistersResource( List<Register> registers );
}
