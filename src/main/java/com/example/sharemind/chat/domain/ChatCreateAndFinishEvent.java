package com.example.sharemind.chat.domain;

import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatCreateAndFinishEvent {

    private final Long chatId;
    private final Long customerId;
    private final Long counselorId;

    public static ChatCreateAndFinishEvent of(Long chatId, Long customerId, Long counselorId) {
        return new ChatCreateAndFinishEvent(chatId, customerId, counselorId);
    }
}
