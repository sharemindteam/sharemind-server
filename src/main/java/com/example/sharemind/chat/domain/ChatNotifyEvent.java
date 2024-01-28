package com.example.sharemind.chat.domain;

import com.example.sharemind.chat.content.ChatRoomStatus;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatNotifyEvent {

    private final Long chatId;
    private final Long customerId;
    private final Long counselorId;
    private final ChatRoomStatus chatRoomStatus;

    public static ChatNotifyEvent of(Long chatId, Long customerId, Long counselorId, ChatRoomStatus chatRoomStatus) {
        return new ChatNotifyEvent(chatId, customerId, counselorId, chatRoomStatus);
    }
}
