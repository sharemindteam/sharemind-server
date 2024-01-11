package com.example.sharemind.letterMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letterMessage.dto.request.LetterMessageCreateRequest;
import com.example.sharemind.letterMessage.dto.request.LetterMessageGetIsSavedRequest;
import com.example.sharemind.letterMessage.dto.request.LetterMessageUpdateRequest;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetIsSavedResponse;

public interface LetterMessageService {
    void createLetterMessage(LetterMessageCreateRequest letterMessageCreateRequest, Customer customer);

    void updateLetterMessage(LetterMessageUpdateRequest letterMessageUpdateRequest, Customer customer);

    LetterMessageGetIsSavedResponse getIsSaved(LetterMessageGetIsSavedRequest letterMessageGetIsSavedRequest);
}
