package com.example.sharemind.letter.application;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;

public interface LetterService {
    Letter createLetter();

    Letter getLetterByLetterId(Long letterId);

    LetterGetCounselorCategoriesResponse getCounselorCategories(Long letterId);

    LetterGetNicknameCategoryResponse getCustomerNicknameAndCategory(Long letterId);
}
