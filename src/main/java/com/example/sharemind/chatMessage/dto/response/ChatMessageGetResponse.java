package com.example.sharemind.chatMessage.dto.response;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.consult.domain.Consult;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageGetResponse {

    private final String customerNickname;

    private final String counselorNickname;

    private final Long messageId;

    private final String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime sendTime;

    private final Boolean isCustomer;

    public static ChatMessageGetResponse of(Chat chat, ChatMessage chatMessage) {
        Consult consult = chat.getConsult();
        return new ChatMessageGetResponse(consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                chatMessage.getMessageId(), chatMessage.getContent(), chatMessage.getUpdatedAt(),
                chatMessage.getIsCustomer());
    }
}
