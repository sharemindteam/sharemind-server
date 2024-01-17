package com.example.sharemind.chat.dto.response;

import com.example.sharemind.chat.content.ChatWebsocketStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUpdateStatusResponse {

    private final ChatWebsocketStatus chatWebsocketStatus;

    private final LocalDateTime localDateTime;

    public static ChatUpdateStatusResponse of(ChatWebsocketStatus chatWebsocketStatus) {
        return new ChatUpdateStatusResponse(chatWebsocketStatus, LocalDateTime.now());
    }
}
