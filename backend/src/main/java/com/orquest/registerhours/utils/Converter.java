package com.orquest.registerhours.utils;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class Converter {

    public static long converterTimeToMilli( ZonedDateTime zonedDateTime ) {

        return TimeUnit.MILLISECONDS.convert(zonedDateTime.getHour(), TimeUnit.HOURS) +
                TimeUnit.MILLISECONDS.convert(zonedDateTime.getMinute(), TimeUnit.MINUTES) +
                TimeUnit.MILLISECONDS.convert(zonedDateTime.getSecond(), TimeUnit.SECONDS);
    }

}
