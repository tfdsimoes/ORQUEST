package com.orquest.registerhours;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties( prefix = "register-hours" )
public class RegisterHoursProperties {

    public long maxHoursDay;

    public long minMonday;

    public long minTuesday;

    public long minWednesday;

    public long minThursday;

    public long minFriday;

    public long minSaturday;

    public long minSunday;
}
