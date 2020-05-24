package com.orquest.registerhours.controller.resources.register;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orquest.registerhours.domain.register.Register;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDayResource {

    @JsonProperty
    private long totalWork;

    @JsonProperty
    @JsonFormat( pattern = "yyyy-MM-dd" )
    private LocalDate date;

    @JsonProperty
    @Builder.Default
    private List<RegisterResource> registers = new ArrayList<>();
}
