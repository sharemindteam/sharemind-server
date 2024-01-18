package com.example.sharemind.letter.dto.response;

import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import com.example.sharemind.global.utils.TimeUtil;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import io.swagger.v3.oas.annotations.media.Schema;
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

        if (recentMessage == null) {
            return ChatLetterGetResponse.of(new LetterGetResponse(letter.getLetterId(), letterStatus,
                    letter.getConsult().getCounselor().getConsultStyle().getDisplayName(), opponentName,
                    null, null));
        }

        return ChatLetterGetResponse.of(new LetterGetResponse(letter.getLetterId(), letterStatus,
                letter.getConsult().getCounselor().getConsultStyle().getDisplayName(), opponentName,
                TimeUtil.getUpdatedAt((recentMessage.getUpdatedAt())), recentMessage.getContent()));
    }
}
