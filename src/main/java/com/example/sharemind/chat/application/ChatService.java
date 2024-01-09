package com.example.sharemind.chat.application;

import java.util.List;

public interface ChatService {
    List<Long> getChat(Long customerId, Boolean isCustomer);
}
