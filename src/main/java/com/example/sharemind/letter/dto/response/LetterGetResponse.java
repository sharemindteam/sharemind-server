package com.example.sharemind.letter.dto.response;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetResponse {

    private final Long letterId;

    private final String letterStatus;

    private final String opponentName;

    private final String updatedAt;

    private final String recentContent;

    public static LetterGetResponse of(Letter letter, LetterMessage recentMessage, Boolean isCustomer) {
        String letterStatus;
        String opponentName;
        if (isCustomer) {
            letterStatus = letter.getLetterStatus().getCustomerDisplayName();
            opponentName = letter.getConsult().getCounselor().getNickname();
        } else {
            letterStatus = letter.getLetterStatus().getCounselorDisplayName();
            opponentName = letter.getConsult().getCustomer().getNickname();
        }

        if (recentMessage == null) {
            return new LetterGetResponse(letter.getLetterId(), letterStatus, opponentName, null, null);
        }

        return new LetterGetResponse(letter.getLetterId(), letterStatus, opponentName, getUpdatedAt(recentMessage.getUpdatedAt()), recentMessage.getContent());
    }

    private static String getUpdatedAt(LocalDateTime updatedAt) {
        LocalDateTime now = LocalDateTime.now();

        if (ChronoUnit.DAYS.between(now, updatedAt) > 0) {
            return updatedAt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        } else if (ChronoUnit.HOURS.between(now, updatedAt) > 0) {
            return ChronoUnit.HOURS.between(now, updatedAt) + "시간 전";
        } else {
            return ChronoUnit.MINUTES.between(now, updatedAt) + "분 전";
        }
    }
}
