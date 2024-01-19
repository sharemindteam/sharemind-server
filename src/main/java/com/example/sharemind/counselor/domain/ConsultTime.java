package com.example.sharemind.counselor.domain;

import com.example.sharemind.counselor.content.DayOfWeek;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConsultTime {
    private static final String SPLIT_HOURS = "~";

    private DayOfWeek day;

    private Set<String> times;

    @Builder
    public ConsultTime(String day, List<String> times) {
        validateTimes(times);

        this.day = DayOfWeek.getDayOfWeekByName(day);
        this.times = Set.copyOf(times);
    }

    private void validateTimes(List<String> times) {
        if (times.size() > 2) {
            throw new CounselorException(CounselorErrorCode.CONSULT_TIME_OVERFLOW);
        } else if (times.size() == 2) {
            int[] time1 = Arrays.stream(times.get(0).split(SPLIT_HOURS)).mapToInt(Integer::parseInt).toArray();
            int[] time2 = Arrays.stream(times.get(1).split(SPLIT_HOURS)).mapToInt(Integer::parseInt).toArray();
            if (!(time1[1] <= time2[0] || time2[1] <= time1[0])) {
                throw new CounselorException(CounselorErrorCode.CONSULT_TIME_DUPLICATE);
            }
        }
    }
}
