package ru.lapushkin.MySecondTestAppSpringBoot.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateTimeUtil {

    public static SimpleDateFormat getCustomFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

}
