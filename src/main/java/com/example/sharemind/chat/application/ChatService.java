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

    void validateChat(Long chatId, Map<String, Object> sessionAttributes, Boolean isCustomer);

    List<ChatLetterGetResponse> getChatInfoByCustomerId(Long customerId, Boolean isCustomer);

    void getAndSendChatStatus(Long chatId, ChatStatusUpdateRequest chatStatusUpdateRequest,
                              Boolean isCustomer);

    void validateNotFinishChat(Long chatId);
}
