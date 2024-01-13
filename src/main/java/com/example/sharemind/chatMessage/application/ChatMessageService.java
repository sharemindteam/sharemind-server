package com.example.sharemind.chatMessage.application;

import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;

public interface ChatMessageService {
    void createAndSendChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId,
                                  Boolean isCustomer, String nickname);

    void createChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId,
                           Boolean isCustomer);
}
