package com.example.sharemind.chat.domain;

import com.example.sharemind.chat.content.ChatWebsocketStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUpdateStatusEvent {

    private final Long chatId;
    private final ChatWebsocketStatus chatWebsocketStatus;

    public static ChatUpdateStatusEvent of(Long chatId, ChatWebsocketStatus chatWebsocketStatus) {
        return new ChatUpdateStatusEvent(chatId, chatWebsocketStatus);
    }
}
