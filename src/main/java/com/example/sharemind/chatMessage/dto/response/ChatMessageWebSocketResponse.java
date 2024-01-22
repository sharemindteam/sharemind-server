package com.example.sharemind.chatMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageWebSocketResponse {

    private final String senderName;

    private final String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime sendTime;

    private final Boolean isCustomer;

    public static ChatMessageWebSocketResponse of(String senderName, String content, Boolean isCustomer) {
        return new ChatMessageWebSocketResponse(senderName, content, LocalDateTime.now(),
                isCustomer);
    }
}
