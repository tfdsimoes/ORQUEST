package com.orquest.registerhours.controller.resources.register;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Object that represents the request to insert a new register
 */
@Getter
@Setter
public class RegisterNewRequest {

    @JsonProperty
    private String businessId;

    @JsonProperty
    @JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX" )
    private ZonedDateTime date;

    @JsonProperty
    private String employeeId;

    @JsonProperty
    private RegisterRecordType recordType;

    @JsonProperty
    private String serviceId;

    @JsonProperty
    private RegisterType type;

}
