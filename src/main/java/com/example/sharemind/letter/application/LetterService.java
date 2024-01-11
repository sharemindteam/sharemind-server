package com.example.sharemind.letter.application;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;

public interface LetterService {
    Letter createLetter();

    Letter getLetterByLetterId(Long letterId);

    LetterGetCounselorCategoriesResponse getCounselorCategories(Long letterId);
}
