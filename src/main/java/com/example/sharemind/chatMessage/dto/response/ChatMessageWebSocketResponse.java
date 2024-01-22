package com.example.sharemind.chatMessage.dto.response;

import com.example.sharemind.global.utils.TimeUtil;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageWebSocketResponse {

    private final String senderName;

    private final String content;

    private final String sendTime;

    private final Boolean isCustomer;

    public static ChatMessageWebSocketResponse of(String senderName, String content, Boolean isCustomer) {
        return new ChatMessageWebSocketResponse(senderName, content, TimeUtil.getUpdatedAt(LocalDateTime.now()),
                isCustomer);
    }
}
