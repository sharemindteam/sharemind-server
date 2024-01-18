package com.example.sharemind.letter.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetDeadlineResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;
import com.example.sharemind.letter.dto.response.LetterGetResponse;

import java.util.List;

public interface LetterService {
    Letter createLetter();

    Letter getLetterByLetterId(Long letterId);

    LetterGetCounselorCategoriesResponse getCounselorCategories(Long letterId);

    LetterGetNicknameCategoryResponse getCustomerNicknameAndCategory(Long letterId);

    LetterGetDeadlineResponse getDeadline(Long letterId);

    List<LetterGetResponse> getLetters(Boolean filter, Boolean isCustomer, String sortType, Customer customer);
}
