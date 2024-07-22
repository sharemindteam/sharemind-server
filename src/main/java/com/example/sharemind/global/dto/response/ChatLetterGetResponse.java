package com.example.sharemind.global.dto.response;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.utils.ChatMessageUtil;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.utils.*;
import com.example.sharemind.letter.dto.response.LetterGetResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.example.sharemind.global.constants.Constants.IS_CHAT;
import static com.example.sharemind.global.constants.Constants.IS_LETTER;

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

    @Schema(description = "마지막 업데이트 일시", example = "02월 01일")
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

    @Schema(description = "채팅/편지 여부")
    private final Boolean isChat;

    public static ChatLetterGetResponse of(LetterGetResponse letterGetResponse) {
        return new ChatLetterGetResponse(letterGetResponse.getLetterId(), letterGetResponse.getConsultStyle(),
                letterGetResponse.getLetterStatus(), letterGetResponse.getOpponentName(),
                letterGetResponse.getUpdatedAt(), letterGetResponse.getRecentContent(), null,
                null, letterGetResponse.getReviewCompleted(), letterGetResponse.getConsultId(),
                IS_LETTER);
    }

    public static ChatLetterGetResponse of(String nickname, int unreadMessageCount, Chat chat, Counselor counselor,
                                           ChatMessage chatMessage, Boolean isCustomer) {
        Boolean reviewCompleted = null;

        if (chatMessage == null) {
            return new ChatLetterGetResponse(chat.getChatId(), counselor.getConsultStyle().getDisplayName(),
                    chat.changeChatStatusForChatList().getDisplayName(), nickname,
                    TimeUtil.getUpdatedAt(chat.getConsult().getUpdatedAt()),
                    counselor.getNickname() + "님께 고민내용을 남겨주세요. " + counselor.getNickname() + "님이 24시간 내에 채팅 요청을 드립니다.",
                    null, 0, reviewCompleted, chat.getConsult().getConsultId(),
                    IS_CHAT);
        }
        String chatMessageContent = chatMessage.getContent();
        if (chat.getChatStatus().equals(ChatStatus.FINISH)) {
            reviewCompleted = chat.getConsult().getReview().getIsCompleted();
        }
        if (chatMessage.getMessageStatus().equals(ChatMessageStatus.SEND_REQUEST) && !isCustomer) {
            chatMessageContent = ChatMessageUtil.getCounselorSendRequestMessage(chat);
        }
        if (chatMessage.getMessageStatus().equals(ChatMessageStatus.FINISH)) {
            chatMessageContent = ChatMessageUtil.getFinishMessage(chat, chatMessageContent, isCustomer);
        }
        return new ChatLetterGetResponse(chat.getChatId(), counselor.getConsultStyle().getDisplayName(),
                chat.changeChatStatusForChatList().getDisplayName(), nickname,
                TimeUtil.getUpdatedAt(chatMessage.getUpdatedAt()),
                chatMessageContent, chatMessage.getIsCustomer(), unreadMessageCount, reviewCompleted,
                chat.getConsult().getConsultId(), IS_CHAT);
    }
}
