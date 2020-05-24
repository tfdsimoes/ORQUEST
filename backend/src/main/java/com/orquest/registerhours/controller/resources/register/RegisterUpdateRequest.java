package com.orquest.registerhours.controller.resources.register;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Class that represents the object to update a register
 */
@Getter
@Setter
public class RegisterUpdateRequest {

    @JsonProperty
    @JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX" )
    private ZonedDateTime date;

    @JsonProperty
    private RegisterRecordType recordType;

    @JsonProperty
    private RegisterType type;
}
