package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.dto.response.ChatInfoGetResponse;
import java.util.List;
import java.util.Map;

public interface ChatService {
    List<Long> getChatsByUserId(Long userId, Boolean isCustomer);

    Chat getChatByChatId(Long chatId);

    void validateChat(Map<String, Object> sessionAttributes, Long chatId);

    List<ChatInfoGetResponse> getChatInfoByCustomerId(Long customerId, Boolean isCustomer);
}
