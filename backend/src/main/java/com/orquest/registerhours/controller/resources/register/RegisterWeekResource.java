package com.orquest.registerhours.controller.resources.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterWeekResource {

    @JsonProperty
    private long totalWork;

    @JsonProperty
    @Builder.Default
    private List<RegisterDayResource> registersDay = new ArrayList<>();
}
