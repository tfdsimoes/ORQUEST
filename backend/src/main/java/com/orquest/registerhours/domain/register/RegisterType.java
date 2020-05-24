package com.orquest.registerhours.domain.register;

public enum RegisterType {
    WORK,
    REST;

    public static RegisterType fromString( String registerType ) {
        for ( RegisterType value : RegisterType.values() ) {
            if ( value.name().equals(registerType) ) {
                return value;
            }
        }

        throw new RuntimeException("Register type does not exist");
    }
}
