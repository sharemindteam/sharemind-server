package com.example.sharemind.letter.application;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.repository.ConsultRepository;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;
import com.example.sharemind.letter.dto.response.LetterGetResponse;
import com.example.sharemind.letter.exception.LetterErrorCode;
import com.example.sharemind.letter.exception.LetterException;
import com.example.sharemind.letter.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

    private final ConsultRepository consultRepository;
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

    @Override
    public List<LetterGetResponse> getLetters(Customer customer, Boolean isCustomer) {
        List<Letter> letters = new ArrayList<>();
        if (isCustomer) {
            List<Consult> consults = consultRepository.findAllByCustomerAndConsultType(customer, ConsultType.LETTER);
            for (Consult consult : consults) {
                letters.add(consult.getLetter());
            }
        } else {
            List<Consult> consults = consultRepository.findAllByCounselorAndConsultType(customer.getCounselor(), ConsultType.LETTER);
            for (Consult consult : consults) {
                letters.add(consult.getLetter());
            }
        }

        return letters.stream()
                .map(letter -> LetterGetResponse.of(letter, isCustomer))
                .toList();
    }
}
