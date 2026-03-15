package com.framework.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * DateUtils - common date/time formatting and manipulation helpers.
 */
public class DateUtils {

    public static final String FORMAT_DATE         = "yyyy-MM-dd";
    public static final String FORMAT_DATE_US      = "MM/dd/yyyy";
    public static final String FORMAT_DATETIME     = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TIMESTAMP    = "yyyyMMdd_HHmmss";
    public static final String FORMAT_DISPLAY      = "dd MMM yyyy";

    private DateUtils() {}

    public static String today() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(FORMAT_DATE));
    }

    public static String todayFormatted(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMAT_DATETIME));
    }

    public static String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMAT_TIMESTAMP));
    }

    public static String addDays(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(FORMAT_DATE));
    }

    public static String subtractDays(int days) {
        return LocalDate.now().minusDays(days).format(DateTimeFormatter.ofPattern(FORMAT_DATE));
    }

    public static long daysBetween(String startDate, String endDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(FORMAT_DATE);
        return ChronoUnit.DAYS.between(LocalDate.parse(startDate, fmt), LocalDate.parse(endDate, fmt));
    }

    public static String format(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parse(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().getValue() >= 6;
    }

    public static String getTimeZone() {
        return ZoneId.systemDefault().getId();
    }
}
