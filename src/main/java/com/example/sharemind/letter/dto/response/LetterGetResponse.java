package com.example.sharemind.letter.dto.response;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "편지 아이디")
    private final Long letterId;

    @Schema(description = "편지 진행 상태", example = "답변 대기")
    private final String letterStatus;

    @Schema(description = "대화 상대방 닉네임", example = "사용자37482")
    private final String opponentName;

    @Schema(description = "마지막 업데이트 일시", example = "8분 전")
    private final String updatedAt;

    @Schema(description = "마지막 업데이트 내용", example = "안녕하세요, 어쩌구저쩌구~")
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
