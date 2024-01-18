package com.example.sharemind.chat.dto.response;

import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUpdateStatusResponse {

    private final ChatWebsocketStatus chatWebsocketStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime localDateTime;

    public static ChatUpdateStatusResponse of(ChatWebsocketStatus chatWebsocketStatus) {
        return new ChatUpdateStatusResponse(chatWebsocketStatus, LocalDateTime.now());
    }
}
