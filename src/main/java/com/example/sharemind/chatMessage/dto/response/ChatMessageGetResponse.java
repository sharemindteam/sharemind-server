package com.example.sharemind.chatMessage.dto.response;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.global.utils.TimeUtil;
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

    private final String sendTime;

    private final Boolean isCustomer;

    public static ChatMessageGetResponse of(Chat chat, ChatMessage chatMessage) {
        Consult consult = chat.getConsult();
        return new ChatMessageGetResponse(consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                chatMessage.getMessageId(), chatMessage.getContent(), TimeUtil.getUpdatedAt(chatMessage.getUpdatedAt()),
                chatMessage.getIsCustomer());
    }
}
