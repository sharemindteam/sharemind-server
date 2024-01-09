package com.example.sharemind.letterMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letterMessage.dto.request.LetterMessageCreateRequest;

public interface LetterMessageService {
    void createLetterMessage(LetterMessageCreateRequest letterMessageCreateRequest, Customer customer);
}
