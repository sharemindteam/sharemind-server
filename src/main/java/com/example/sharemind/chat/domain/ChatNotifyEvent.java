package com.example.sharemind.chat.domain;

import com.example.sharemind.chat.content.ChatRoomWebsocketStatus;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatNotifyEvent {

    private final Long chatId;
    private final Long customerId;
    private final Long counselorId;
    private final ChatRoomWebsocketStatus chatRoomWebsocketStatus;

    public static ChatNotifyEvent of(Long chatId, Long customerId, Long counselorId, ChatRoomWebsocketStatus chatRoomWebsocketStatus) {
        return new ChatNotifyEvent(chatId, customerId, counselorId, chatRoomWebsocketStatus);
    }
}
