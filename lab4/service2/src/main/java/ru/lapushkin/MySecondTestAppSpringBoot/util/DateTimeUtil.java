package ru.lapushkin.MySecondTestAppSpringBoot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

    public static SimpleDateFormat getCustomFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    /**
     * Сколько миллисекунд прошло с момента systemTime до текущего момента
     */
    public static long millisSince(String systemTime) throws ParseException {
        return new Date().getTime() - getCustomFormat().parse(systemTime).getTime();
    }

}
