package com.example.sharemind.nonRealtimeMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.nonRealtimeMessage.dto.request.LetterMessageCreateRequest;
import com.example.sharemind.nonRealtimeMessage.dto.request.LetterMessageUpdateRequest;

public interface LetterMessageService {
    void createLetterMessage(LetterMessageCreateRequest letterMessageCreateRequest, Customer customer);

    void updateLetterMessage(LetterMessageUpdateRequest letterMessageUpdateRequest, Customer customer);
}
