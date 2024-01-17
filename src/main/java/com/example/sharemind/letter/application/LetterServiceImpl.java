package com.example.sharemind.letter.application;

import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.letter.content.LetterSortType;
import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;
import com.example.sharemind.letter.dto.response.LetterGetResponse;
import com.example.sharemind.letter.exception.LetterErrorCode;
import com.example.sharemind.letter.exception.LetterException;
import com.example.sharemind.letter.repository.LetterRepository;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.example.sharemind.letterMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.letterMessage.exception.LetterMessageException;
import com.example.sharemind.letterMessage.repository.LetterMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {
    private static final Boolean IS_COMPLETED = true;
    private static final Integer FINISH_AT_FIRST_MESSAGES = 2;

    private final ConsultService consultService;
    private final LetterRepository letterRepository;
    private final LetterMessageRepository letterMessageRepository;

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
    public List<LetterGetResponse> getLetters(Boolean filter, Boolean isCustomer, String letterSortType, Customer customer) {
        LetterSortType sortType = LetterSortType.getSortTypeByName(letterSortType);

        List<Consult> consults;
        if (isCustomer) {
            consults = consultService.getConsultsByCustomerIdAndConsultTypeAndIsPaid(
                    customer.getCustomerId(), ConsultType.LETTER);
        } else {
            consults = consultService.getConsultsByCounselorIdAndConsultTypeAndIsPaid(
                    customer.getCounselor().getCounselorId(), ConsultType.LETTER);
        }

        List<Letter> letters;
        if (filter) {
            letters = consults.stream()
                    .map(Consult::getLetter)
                    .filter(letter -> (letter.getLetterStatus() != LetterStatus.FINISH) && (letter.getLetterStatus() != LetterStatus.CANCEL))
                    .collect(Collectors.toList());
        } else {
            letters = consults.stream()
                    .map(Consult::getLetter)
                    .collect(Collectors.toList());
        }

        switch (sortType) {
            case LATEST -> letters.sort((letter1, letter2) -> letter2.getUpdatedAt().compareTo(letter1.getUpdatedAt()));
            case UNREAD -> letters.sort((letter1, letter2) -> {
                Boolean checkLetter1 = checkLetterReadAll(letter1, isCustomer);
                Boolean checkLetter2 = checkLetterReadAll(letter2, isCustomer);

                if (checkLetter1.equals(checkLetter2)) {
                    return letter2.getUpdatedAt().compareTo(letter1.getUpdatedAt());
                } else {
                    if (checkLetter1) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }

        return letters.stream()
                .map(letter -> LetterGetResponse.of(letter, getRecentMessage(letter), isCustomer))
                .toList();
    }

    private Boolean checkLetterReadAll(Letter letter, Boolean isCustomer) {
        Boolean readAllLetter = null;

        switch (letter.getLetterStatus()) {
            case WAITING, CANCEL -> readAllLetter = true;
            case FIRST_ASKING, SECOND_ASKING -> {
                if (isCustomer) {
                    readAllLetter = true;
                } else {
                    LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByStatus(letter.getLetterStatus());
                    LetterMessage recentMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(letter, messageType, IS_COMPLETED)
                            .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));

                    readAllLetter = letter.getCounselorReadId().equals(recentMessage.getMessageId());
                }
            }
            case FIRST_ANSWER -> {
                if (isCustomer) {
                    LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByStatus(letter.getLetterStatus());
                    LetterMessage recentMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(letter, messageType, IS_COMPLETED)
                            .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));

                    readAllLetter = letter.getCustomerReadId().equals(recentMessage.getMessageId());
                } else {
                    readAllLetter = true;
                }
            }
            case FINISH -> {
                if (isCustomer) {
                    LetterMessage recentMessage;
                    if (letterMessageRepository.countByLetterAndIsCompletedIsTrueAndIsActivatedIsTrue(letter).equals(FINISH_AT_FIRST_MESSAGES)) {
                        recentMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(letter, LetterMessageType.FIRST_REPLY, IS_COMPLETED)
                                .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));
                    } else {
                        recentMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(letter, LetterMessageType.SECOND_REPLY, IS_COMPLETED)
                                .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));
                    }

                    readAllLetter = letter.getCustomerReadId().equals(recentMessage.getMessageId());
                } else {
                    readAllLetter = true;
                }
            }
        }

        return readAllLetter;
    }

    private LetterMessage getRecentMessage(Letter letter) {
        LetterMessageType recentType = null;
        switch (letterMessageRepository.countByLetterAndIsCompletedIsTrueAndIsActivatedIsTrue(letter)) {
            case 0 -> {
                return null;
            }
            case 1 -> recentType = LetterMessageType.FIRST_QUESTION;
            case 2 -> recentType = LetterMessageType.FIRST_REPLY;
            case 3 -> recentType = LetterMessageType.SECOND_QUESTION;
            case 4 -> recentType = LetterMessageType.SECOND_REPLY;
        }

        return letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(letter, recentType, IS_COMPLETED)
                .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));
    }
}
