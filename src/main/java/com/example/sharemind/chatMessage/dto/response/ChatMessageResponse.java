package com.example.sharemind.chatMessage.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    private final String senderName;
    private final String content;
    private final String sendTime;
    private final Boolean isCustomer;

    private ChatMessageResponse(String senderName, String content, LocalDateTime sendTime, Boolean isCustomer) {
        this.senderName = senderName;
        this.content = content;
        this.sendTime = sendTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.isCustomer = isCustomer;
    }

    public static ChatMessageResponse of(String senderName, String content, Boolean isCustomer) {
        return new ChatMessageResponse(senderName, content, LocalDateTime.now(), isCustomer);
    }
}
