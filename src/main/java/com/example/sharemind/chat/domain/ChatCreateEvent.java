package com.example.sharemind.chat.domain;

import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatCreateEvent {

    private final Long chatId;
    private final Long customerId;
    private final Long counselorId;

    public static ChatCreateEvent of(Long chatId, Long customerId, Long counselorId) {
        return new ChatCreateEvent(chatId, customerId, counselorId);
    }
}
