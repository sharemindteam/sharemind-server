package com.example.sharemind.letterMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.example.sharemind.letterMessage.dto.request.*;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetIsSavedResponse;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetResponse;

public interface LetterMessageService {
    LetterMessage createLetterMessage(LetterMessageCreateRequest letterMessageCreateRequest, Customer customer);

    LetterMessage updateLetterMessage(LetterMessageUpdateRequest letterMessageUpdateRequest, Customer customer);

    void createFirstQuestion(LetterMessageCreateFirstRequest letterMessageCreateFirstRequest, Customer customer);

    void updateFirstQuestion(LetterMessageUpdateFirstRequest letterMessageUpdateFirstRequest, Customer customer);

    LetterMessageGetIsSavedResponse getIsSaved(LetterMessageGetIsSavedRequest letterMessageGetIsSavedRequest);

    LetterMessageGetResponse getLetterMessage(LetterMessageGetRequest letterMessageGetRequest);
}
