package com.example.sharemind.chat.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChattingResponse {
    private final String senderName;
    private final String content;
    private final String sendTime;

    private ChattingResponse(String senderName, String content, LocalDateTime sendTime) {
        this.senderName = senderName;
        this.content = content;
        this.sendTime = sendTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static ChattingResponse of(String content, String senderName) {
        return new ChattingResponse(senderName, content, LocalDateTime.now());
    }
}
