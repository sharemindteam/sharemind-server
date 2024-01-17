package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.dto.request.ChatStatusUpdateRequest;
import com.example.sharemind.chat.dto.response.ChatGetStatusResponse;
import com.example.sharemind.chat.dto.response.ChatInfoGetResponse;
import com.example.sharemind.consult.domain.Consult;
import java.util.List;
import java.util.Map;

public interface ChatService {
    Chat createChat(Consult consult);

    List<Long> getChatsByUserId(Long userId, Boolean isCustomer);

    Chat getChatByChatId(Long chatId);

    void validateChat(Long chatId, Map<String, Object> sessionAttributes, Boolean isCustomer);

    List<ChatInfoGetResponse> getChatInfoByCustomerId(Long customerId, Boolean isCustomer);

    ChatGetStatusResponse getAndUpdateChatStatus(Long chatId, ChatStatusUpdateRequest chatStatusUpdateRequest,
                                                 Boolean isCustomer);
}
