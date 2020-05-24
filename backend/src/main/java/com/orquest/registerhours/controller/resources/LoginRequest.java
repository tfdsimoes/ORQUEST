package com.orquest.registerhours.controller.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @JsonProperty
    private String number;
}
