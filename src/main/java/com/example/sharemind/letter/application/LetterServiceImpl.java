package com.example.sharemind.letter.application;

import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import com.example.sharemind.global.content.ChatLetterSortType;
import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetDeadlineResponse;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {
    private static final Boolean IS_COMPLETED = true;

    private final CustomerService customerService;
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
    public String getCustomerCategory(Long letterId) {
        Letter letter = getLetterByLetterId(letterId);

        return letter.getConsultCategory().getDisplayName();
    }

    @Override
    public LetterGetNicknameCategoryResponse getCustomerNicknameAndCategory(Long letterId) {
        Letter letter = getLetterByLetterId(letterId);

        return LetterGetNicknameCategoryResponse.of(letter);
    }

    @Override
    public LetterGetDeadlineResponse getDeadline(Long letterId) {
        Letter letter = getLetterByLetterId(letterId);

        return LetterGetDeadlineResponse.of(letter);
    }

    @Override
    public List<ChatLetterGetResponse> getLetters(Boolean filter, Boolean isCustomer, String letterSortType, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        ChatLetterSortType sortType = ChatLetterSortType.getSortTypeByName(letterSortType);

        List<Consult> consults;
        if (isCustomer) {
            consults = consultService.getConsultsByCustomerIdAndConsultTypeAndIsPaid(
                    customer.getCustomerId(), ConsultType.LETTER);
        } else {
            Counselor counselor = customer.getCounselor();
            if (counselor == null) {
                throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_FOUND);
            }

            consults = consultService.getConsultsByCounselorIdAndConsultTypeAndIsPaid(
                    counselor.getCounselorId(), ConsultType.LETTER);
        }

        List<Letter> letters;
        if (filter) {
            letters = consults.stream()
                    .map(Consult::getLetter)
                    .filter(letter -> (letter.getLetterStatus() != LetterStatus.FIRST_FINISH) &&
                            (letter.getLetterStatus() != LetterStatus.SECOND_FINISH) &&
                            (letter.getLetterStatus() != LetterStatus.COUNSELOR_CANCEL) &&
                            (letter.getLetterStatus() != LetterStatus.CUSTOMER_CANCEL))
                    .collect(Collectors.toList());
        } else {
            letters = consults.stream()
                    .map(Consult::getLetter)
                    .collect(Collectors.toList());
        }

        Comparator<Letter> comparator = (letter1, letter2) -> {
            LetterMessage recentMessage1 = getRecentMessage(letter1);
            LetterMessage recentMessage2 = getRecentMessage(letter2);

            LocalDateTime updated1 = (recentMessage1 == null) ? letter1.getUpdatedAt() : recentMessage1.getUpdatedAt();
            LocalDateTime updated2 = (recentMessage2 == null) ? letter2.getUpdatedAt() : recentMessage2.getUpdatedAt();

            return updated2.compareTo(updated1);
        };
        switch (sortType) {
            case LATEST -> letters.sort(comparator);
            case UNREAD -> letters.sort((letter1, letter2) -> {
                Boolean checkLetter1 = checkLetterReadAll(letter1, isCustomer);
                Boolean checkLetter2 = checkLetterReadAll(letter2, isCustomer);

                if (checkLetter1.equals(checkLetter2)) {
                    return comparator.compare(letter1, letter2);
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

    @Override
    public String getOpponentNickname(Long letterId, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Consult consult = getLetterByLetterId(letterId).getConsult();
        if (consult.getCustomer().equals(customer)) {
            return consult.getCounselor().getNickname();
        } else if (consult.getCounselor().equals(customer.getCounselor())) {
            return consult.getCustomer().getNickname();
        } else {
            throw new LetterException(LetterErrorCode.INVALID_LETTER_PARTICIPANT, customerId.toString());
        }
    }

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkLettersDeadline() {
        letterRepository.findAll().stream()
                .filter(letter -> ((letter.getLetterStatus() == LetterStatus.FIRST_ASKING) ||
                        (letter.getLetterStatus() == LetterStatus.FIRST_ANSWER) ||
                        (letter.getLetterStatus() == LetterStatus.SECOND_ASKING))
                        && letter.getDeadline().isBefore(LocalDateTime.now()) && letter.isActivated())
                .forEach(letter -> {
                    if (letter.getLetterStatus() == LetterStatus.FIRST_ANSWER) {
                        letter.updateLetterStatusFirstFinish();
                    } else {
                        letter.updateLetterStatusCounselorCancel();
                    }
                });
    }

    private Boolean checkLetterReadAll(Letter letter, Boolean isCustomer) {
        Boolean readAllLetter = null;

        switch (letter.getLetterStatus()) {
            case WAITING -> readAllLetter = true;
            case FIRST_ASKING, SECOND_ASKING -> {
                if (isCustomer) {
                    readAllLetter = true;
                } else {
                    LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByStatus(letter.getLetterStatus());
                    LetterMessage recentMessage = getRecentMessageWithMessageType(letter, messageType);

                    readAllLetter = recentMessage.getMessageId().equals(letter.getCounselorReadId());
                }
            }
            case FIRST_ANSWER, SECOND_FINISH -> {
                if (isCustomer) {
                    LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByStatus(letter.getLetterStatus());
                    LetterMessage recentMessage = getRecentMessageWithMessageType(letter, messageType);

                    readAllLetter = recentMessage.getMessageId().equals(letter.getCustomerReadId());
                } else {
                    readAllLetter = true;
                }
            }
            case FIRST_FINISH -> {
                if (isCustomer) {
                    LetterMessageType messageType = LetterMessageType.FIRST_REPLY;
                    LetterMessage recentMessage = getRecentMessageWithMessageType(letter, messageType);

                    readAllLetter = recentMessage.getMessageId().equals(letter.getCustomerReadId());
                } else {
                    readAllLetter = true;
                }
            }
            case COUNSELOR_CANCEL -> {
                if (isCustomer) {
                    readAllLetter = true;
                } else {
                    LetterMessage recentMessage = getRecentMessage(letter);

                    readAllLetter = (recentMessage == null) || recentMessage.getMessageId().equals(letter.getCounselorReadId());
                }
            }
            case CUSTOMER_CANCEL -> {
                LetterMessage recentMessage = getRecentMessage(letter);
                if (isCustomer) {
                    readAllLetter = (recentMessage == null) || recentMessage.getMessageId().equals(letter.getCustomerReadId());
                } else {
                    readAllLetter = (recentMessage == null) || recentMessage.getMessageId().equals(letter.getCounselorReadId());
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

        return getRecentMessageWithMessageType(letter, recentType);
    }

    private LetterMessage getRecentMessageWithMessageType(Letter letter, LetterMessageType messageType) {
        return letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(letter, messageType, IS_COMPLETED)
                .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));
    }
}
