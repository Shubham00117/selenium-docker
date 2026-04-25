package com.vinsguru.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public final class DateTimeUtil {

    private static final ZoneId REPORT_ZONE = ZoneId.of(ConfigReader.get("timezone", "Asia/Kolkata"));

    private DateTimeUtil() {
    }

    public static void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(REPORT_ZONE));
        System.setProperty("user.timezone", REPORT_ZONE.getId());
    }

    public static String zoneId() {
        return REPORT_ZONE.getId();
    }

    public static String now(DateTimeFormatter formatter) {
        return ZonedDateTime.now(REPORT_ZONE).format(formatter);
    }
}
