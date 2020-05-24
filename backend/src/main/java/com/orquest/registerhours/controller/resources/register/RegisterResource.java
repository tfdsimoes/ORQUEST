package com.orquest.registerhours.controller.resources.register;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResource {

    @JsonProperty
    private String id;

    @JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ssZ" )
    private ZonedDateTime date;

    @JsonProperty
    private RegisterRecordType recordType;

    @JsonProperty
    private RegisterType type;

}
