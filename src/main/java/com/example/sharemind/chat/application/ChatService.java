package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.dto.request.ChatStatusUpdateRequest;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.global.dto.response.ChatLetterGetResponse;

import java.util.List;
import java.util.Map;

public interface ChatService {
    Chat createChat(Consult consult);

    Chat getChatByChatId(Long chatId);

    void validateChatWithWebSocket(Long chatId, Map<String, Object> sessionAttributes, Boolean isCustomer);

    void getAndSendChatIdsByWebSocket(Map<String, Object> sessionAttributes, Boolean isCustomer);

    List<ChatLetterGetResponse> getChatInfoByCustomerId(Long customerId, Boolean isCustomer, Boolean filter,
                                                        String sortType);

    void getAndSendChatStatus(Long chatId, ChatStatusUpdateRequest chatStatusUpdateRequest, Boolean isCustomer);

    void validateNotFinishChat(Long chatId);

    void validateChat(Chat chat, Boolean isCustomer, Long customerId);

    void updateReadId(Long chatId, Long customerId, Boolean isCustomer);

    Chat getAndValidateChat(Long chatId, Boolean isCustomer, Long customerId);
}
