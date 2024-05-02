package com.example.sharemind.chatMessage.dto.response;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.utils.ChatMessageUtil;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.global.utils.TimeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageGetResponse {

    @Schema(description = "구매자 닉네임")
    private final String customerNickname;

    @Schema(description = "상담사 닉네임")
    private final String counselorNickname;

    @Schema(description = "메세지 id")
    private final Long messageId;

    @Schema(description = "메세지 내용")
    private final String content;

    @Schema(description = "메세지 전송 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime sendTime;

    @Schema(description = "구매자이면 true")
    private final Boolean isCustomer;

    @Schema(description = "채팅 메세지 상태")
    private final ChatMessageStatus chatMessageStatus;

    @Schema(description = "채팅 요청 상태일 때, 남은 시간, 채팅 요청 상태 이외에는 null")
    private final String time;

    public static ChatMessageGetResponse of(Chat chat, ChatMessage chatMessage, Boolean isCustomer) {
        Consult consult = chat.getConsult();
        String messageContent = chatMessage.getContent();
        String sendRequestTime = null;
        if (chatMessage.getMessageStatus() == ChatMessageStatus.SEND_REQUEST) {
            sendRequestTime = TimeUtil.getChatSendRequestLeftTime(chatMessage.getCreatedAt());
            if (!isCustomer) {
                messageContent = ChatMessageUtil.getCounselorSendRequestMessage(chat);
            }
        }
        if (chatMessage.getMessageStatus() == ChatMessageStatus.FINISH) {
            messageContent = ChatMessageUtil.getFinishMessage(chat, chatMessage.getContent(), isCustomer);
        }
        return new ChatMessageGetResponse(consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                chatMessage.getMessageId(), messageContent, chatMessage.getUpdatedAt(),
                chatMessage.getIsCustomer(), chatMessage.getMessageStatus(), sendRequestTime);
    }
}
