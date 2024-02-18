package com.example.sharemind.chatMessage.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.chatMessage.dto.response.ChatMessageGetResponse;

import java.util.List;

public interface ChatMessageService {
    void createAndSendChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId,
                                  Boolean isCustomer, String nickname);

    void createChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId,
                           Boolean isCustomer);

    List<ChatMessageGetResponse> getChatMessage(Long chatId, Long messageId, Long customerId, Boolean isCustomer);

    void createChatNoticeMessage(ChatMessageStatus chatMessageStatus, Chat chat);

    void updateSendRequestMessageIsActivatedFalse(Chat chat);
}
