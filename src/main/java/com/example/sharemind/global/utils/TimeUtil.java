package com.example.sharemind.global.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class TimeUtil {
    public static String getUpdatedAt(LocalDateTime updatedAt) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now();

        if (ChronoUnit.YEARS.between(updatedAt, now) > 0) {
            return updatedAt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withZone(timeZone.toZoneId()));
        } else if (ChronoUnit.DAYS.between(updatedAt, now) > 0) {
            if (ChronoUnit.DAYS.between(updatedAt, now) == 1) {
                return "어제";
            } else {
                return updatedAt.format(DateTimeFormatter.ofPattern("MM.dd").withZone(timeZone.toZoneId()));
            }
        } else {
            return updatedAt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                    .withZone(timeZone.toZoneId()));
        }
    }

    public static String getUpdatedAtForReview(LocalDateTime updatedAt) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now();

        if (ChronoUnit.YEARS.between(updatedAt, now) > 0) {
            return updatedAt.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일").withZone(timeZone.toZoneId()));
        } else {
            return updatedAt.format(DateTimeFormatter.ofPattern("MM월 dd일").withZone(timeZone.toZoneId()));
        }
    }
}
