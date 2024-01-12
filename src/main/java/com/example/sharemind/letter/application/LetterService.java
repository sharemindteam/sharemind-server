package com.example.sharemind.letter.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;
import com.example.sharemind.letter.dto.response.LetterGetResponse;

import java.util.List;

public interface LetterService {
    Letter createLetter();

    Letter getLetterByLetterId(Long letterId);

    LetterGetCounselorCategoriesResponse getCounselorCategories(Long letterId);

    LetterGetNicknameCategoryResponse getCustomerNicknameAndCategory(Long letterId);

    // TODO 임시, 나중에 보완 필요
    List<LetterGetResponse> getLetters(Customer customer, Boolean isCustomer);
}
