package com.example.sharemind.letter.application;

import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetDeadlineResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;

import java.util.List;

public interface LetterService {
    Letter createLetter();

    Letter getLetterByLetterId(Long letterId);

    LetterGetCounselorCategoriesResponse getCounselorCategories(Long letterId);
    String getCustomerCategory(Long letterId);

    LetterGetNicknameCategoryResponse getCustomerNicknameAndCategory(Long letterId);

    LetterGetDeadlineResponse getDeadline(Long letterId);

    List<ChatLetterGetResponse> getLetters(Boolean filter, Boolean isCustomer, String sortType, Long customerId);
}
