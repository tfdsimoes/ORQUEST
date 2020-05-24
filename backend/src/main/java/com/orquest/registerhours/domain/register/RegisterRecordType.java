package com.orquest.registerhours.domain.register;

public enum RegisterRecordType {
    IN,
    OUT;

    public static RegisterRecordType fromString( String registerRecordType ) {
        for ( RegisterRecordType value : RegisterRecordType.values() ) {
            if ( value.name().equals(registerRecordType) ) {
                return value;
            }
        }

        throw new RuntimeException("Register record type does not exist");
    }
}
