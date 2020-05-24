package com.orquest.registerhours.controller.resources.alert;

import com.orquest.registerhours.domain.alert.Alert;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ON_IMPLICIT_CONVERSION;

@Mapper( nullValueCheckStrategy = ON_IMPLICIT_CONVERSION )
public interface AlertResourceMapper {

    AlertResourceMapper INSTANCE = Mappers.getMapper( AlertResourceMapper.class );

    @Mapping( target = "date", source = "date" )
    @Mapping( target = "notes", source = "notes" )
    @Mapping( target = "type", expression = "java(AlertType.valueOf(alert.getType().name()))" )
    AlertResource alertToAlertResource( Alert alert );

    @IterableMapping( qualifiedByName = "reduced" )
    List<AlertResource> alertsToAlertsResource( List<Alert> alerts );
}
