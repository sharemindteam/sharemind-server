package com.example.sharemind.global.dto.response;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.utils.*;
import com.example.sharemind.letter.dto.response.LetterGetResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatLetterGetResponse {

    @Schema(description = "(채팅/편지)아이디")
    private final Long id;

    @Schema(description = "상담 스타일", example = "조언")
    private final String consultStyle;

    @Schema(description = "진행 상태", example = "답변 대기")
    private final String status;

    @Schema(description = "대화 상대방 닉네임", example = "사용자37482")
    private final String opponentNickname;

    @Schema(description = "마지막 업데이트 일시", example = "8분 전")
    private final String latestMessageUpdatedAt;

    @Schema(description = "마지막 업데이트 내용", example = "안녕하세요, 어쩌구저쩌구~")
    private final String latestMessageContent;

    @Schema(description = "마지막 업데이트 작성자가 구매자인지 여부")
    private final Boolean latestMessageIsCustomer;

    @Schema(description = "읽지 않은 메시지 수")
    private final Integer unreadMessageCount;

    @Schema(description = "리뷰 작성 여부")
    private final Boolean reviewCompleted;

    @Schema(description = "상담 아이디")
    private final Long consultId;

    public static ChatLetterGetResponse of(LetterGetResponse letterGetResponse) {
        return new ChatLetterGetResponse(letterGetResponse.getLetterId(), letterGetResponse.getConsultStyle(),
                letterGetResponse.getLetterStatus(), letterGetResponse.getOpponentName(),
                letterGetResponse.getUpdatedAt(), letterGetResponse.getRecentContent(), null,
                null, letterGetResponse.getReviewCompleted(), letterGetResponse.getConsultId());
    }

    public static ChatLetterGetResponse of(String nickname, int unreadMessageCount, Chat chat, Counselor counselor,
                                           ChatMessage chatMessage) {
        Boolean reviewCompleted = null;
        if (chat.getChatStatus().equals(ChatStatus.FINISH)) {
            reviewCompleted = chat.getConsult().getReview().getIsCompleted();
        }

        if (chatMessage == null) {
            return new ChatLetterGetResponse(chat.getChatId(), counselor.getConsultStyle().getDisplayName(),
                    chat.getChatStatus().getDisplayName(), nickname, null, null,
                    null, null, reviewCompleted, chat.getConsult().getConsultId());
        }
        return new ChatLetterGetResponse(chat.getChatId(), counselor.getConsultStyle().getDisplayName(),
                chat.getChatStatus().getDisplayName(), nickname, TimeUtil.getUpdatedAt(chatMessage.getUpdatedAt()),
                chatMessage.getContent(), chatMessage.getIsCustomer(), unreadMessageCount, reviewCompleted,
                chat.getConsult().getConsultId());
    }
}
