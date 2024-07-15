package com.example.sharemind.letter.dto.response;

import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import com.example.sharemind.global.utils.*;
import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetResponse {

    @Schema(description = "편지 아이디")
    private final Long letterId;

    @Schema(description = "편지 진행 상태", example = "답변 대기")
    private final String letterStatus;

    @Schema(description = "상담 스타일", example = "공감")
    private final String consultStyle;

    @Schema(description = "대화 상대방 닉네임", example = "사용자37482")
    private final String opponentName;

    @Schema(description = "마지막 업데이트 일시", example = "8분 전")
    private final String updatedAt;

    @Schema(description = "마지막 업데이트 내용", example = "안녕하세요, 어쩌구저쩌구~")
    private final String recentContent;

    @Schema(description = "리뷰 작성 여부")
    private final Boolean reviewCompleted;

    @Schema(description = "상담 uuid")
    private final UUID consultUuid;

    public static ChatLetterGetResponse of(Letter letter, LetterMessage recentMessage, Boolean isCustomer) {
        String letterStatus;
        String opponentName;
        if (isCustomer) {
            letterStatus = letter.getLetterStatus().getCustomerDisplayName();
            opponentName = letter.getConsult().getCounselor().getNickname();
        } else {
            letterStatus = letter.getLetterStatus().getCounselorDisplayName();
            opponentName = letter.getConsult().getCustomer().getNickname();
        }

        Boolean reviewCompleted = null;
        if (letter.getLetterStatus().equals(LetterStatus.FIRST_FINISH) ||
                letter.getLetterStatus().equals(LetterStatus.SECOND_FINISH)) {
            reviewCompleted = letter.getConsult().getReview().getIsCompleted();
        }

        if (recentMessage == null) {
            return ChatLetterGetResponse.of(new LetterGetResponse(letter.getLetterId(), letterStatus,
                    letter.getConsult().getCounselor().getConsultStyle().getDisplayName(), opponentName,
                    TimeUtil.getUpdatedAt(letter.getConsult().getUpdatedAt()), null, reviewCompleted,
                    letter.getConsult().getConsultUuid()));
        }

        return ChatLetterGetResponse.of(new LetterGetResponse(letter.getLetterId(), letterStatus,
                letter.getConsult().getCounselor().getConsultStyle().getDisplayName(), opponentName,
                TimeUtil.getUpdatedAt((recentMessage.getUpdatedAt())), recentMessage.getContent(), reviewCompleted,
                letter.getConsult().getConsultUuid()));
    }
}
