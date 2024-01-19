package com.example.sharemind.counselor.content;

import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DayOfWeek {

    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT,
    SUN;

    public static DayOfWeek getDayOfWeekByName(String name) {
        return Arrays.stream(DayOfWeek.values())
                .filter(day -> day.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new CounselorException(CounselorErrorCode.DAY_OF_WEEK_NOT_FOUND, name));
    }
}
