package com.example.sharemind.nonRealtimeMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.nonRealtimeMessage.dto.request.LetterMessageCreateRequest;

public interface LetterMessageService {
    void createLetterMessage(LetterMessageCreateRequest letterMessageCreateRequest, Customer customer);
}
