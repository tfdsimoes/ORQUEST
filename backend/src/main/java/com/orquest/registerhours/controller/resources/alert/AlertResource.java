package com.orquest.registerhours.controller.resources.alert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AlertResource {

    @JsonProperty
    private AlertType type;

    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate date;

    @JsonProperty
    private String notes;
}
