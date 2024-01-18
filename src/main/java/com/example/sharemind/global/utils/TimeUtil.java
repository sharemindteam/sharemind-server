package com.example.sharemind.global.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
    public static String getUpdatedAt(LocalDateTime updatedAt) {
        LocalDateTime now = LocalDateTime.now();

        if (ChronoUnit.YEARS.between(updatedAt, now) > 0) {
            return updatedAt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        } else if (ChronoUnit.DAYS.between(updatedAt, now) > 0) {
            if (ChronoUnit.DAYS.between(updatedAt, now) == 1) {
                return "어제";
            } else {
                return updatedAt.format(DateTimeFormatter.ofPattern("MM.dd"));
            }
        } else if (ChronoUnit.HOURS.between(updatedAt, now) > 0) {
            return updatedAt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        } else if (ChronoUnit.MINUTES.between(updatedAt, now) > 0) {
            return ChronoUnit.MINUTES.between(updatedAt, now) + "분 전";
        } else {
            return "방금";
        }
    }
}
