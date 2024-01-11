package com.example.sharemind.chatMessage.application;

import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;

public interface ChatMessageService {
    void createChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId, Boolean isCustomer);
}
