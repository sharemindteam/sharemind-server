package com.example.sharemind.letter.application;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;
import com.example.sharemind.letter.exception.LetterErrorCode;
import com.example.sharemind.letter.exception.LetterException;
import com.example.sharemind.letter.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

    private final LetterRepository letterRepository;

    @Transactional
    @Override
    public Letter createLetter() {
        Letter letter = Letter.builder().build();
        letterRepository.save(letter);

        return letter;
    }

    @Override
    public Letter getLetterByLetterId(Long letterId) {
        return letterRepository.findByLetterIdAndIsActivatedIsTrue(letterId)
                .orElseThrow(() -> new LetterException(LetterErrorCode.LETTER_NOT_FOUND,
                        letterId.toString()));
    }

    @Override
    public LetterGetCounselorCategoriesResponse getCounselorCategories(Long letterId) {
        Letter letter = getLetterByLetterId(letterId);

        return LetterGetCounselorCategoriesResponse.of(letter);
    }

    @Override
    public LetterGetNicknameCategoryResponse getCustomerNicknameAndCategory(Long letterId) {
        Letter letter = getLetterByLetterId(letterId);

        return LetterGetNicknameCategoryResponse.of(letter);
    }
}
