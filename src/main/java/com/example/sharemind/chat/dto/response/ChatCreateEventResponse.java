package com.example.sharemind.chat.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatCreateEventResponse {

    private final Long chatId;
    private final LocalDateTime createTime;

    public static ChatCreateEventResponse of(Long chatId) {
        return new ChatCreateEventResponse(chatId, LocalDateTime.now());
    }
}
