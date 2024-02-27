package com.example.sharemind.global.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

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
        } else {
            return updatedAt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.KOREA));
        }
    }

    public static String getUpdatedAtForReview(LocalDateTime updatedAt) {
        LocalDateTime now = LocalDateTime.now();

        if (ChronoUnit.YEARS.between(updatedAt, now) > 0) {
            return updatedAt.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        } else {
            return updatedAt.format(DateTimeFormatter.ofPattern("MM월 dd일"));
        }
    }

    public static String getChatSendRequestLeftTime(LocalDateTime updatedAt) {
        long gapSeconds = ChronoUnit.SECONDS.between(updatedAt, LocalDateTime.now());
        long totalSeconds = 600 - gapSeconds;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
