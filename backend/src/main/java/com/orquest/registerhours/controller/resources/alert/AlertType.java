package com.orquest.registerhours.controller.resources.alert;

public enum AlertType {
    INCOMPLETE,
    MAX_HOURS,
    START_BEFORE_TIME;

    public static AlertType fromString(String registerRecordType ) {
        for ( AlertType value : AlertType.values() ) {
            if ( value.name().equals(registerRecordType) ) {
                return value;
            }
        }

        throw new RuntimeException("Alert type does not exist");
    }
}
