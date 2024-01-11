package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import java.util.List;

public interface ChatService {
    List<Long> getChatsByUserId(Long customerId, Boolean isCustomer);

    Chat getChatByChatId(Long chatId);
}
