package com.example.sharemind.letter.application;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.global.dto.response.ChatLetterGetOngoingResponse;
import com.example.sharemind.letter.dto.response.LetterGetResponse;
import com.example.sharemind.letter.repository.LetterRepository;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.example.sharemind.letterMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.letterMessage.exception.LetterMessageException;
import com.example.sharemind.letterMessage.repository.LetterMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.sharemind.global.constants.Constants.COUNSELOR_ONGOING_CONSULT;
import static com.example.sharemind.global.constants.Constants.CUSTOMER_ONGOING_CONSULT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterConsultService {
    private static final Boolean IS_COMPLETED = true;

    private final CustomerService customerService;
    private final LetterRepository letterRepository;
    private final LetterMessageRepository letterMessageRepository;

    public ChatLetterGetOngoingResponse getOngoingLetters(Long customerId, Boolean isCustomer) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        List<Letter> letters;
        if (isCustomer) {
            letters = letterRepository.findAllByConsultCustomerAndLetterStatusOngoingOrderByUpdatedAtDesc(
                    customer);
        } else {
            Counselor counselor = customer.getCounselor();
            if (counselor == null) {
                throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_FOUND);
            }

            letters = letterRepository.findAllByConsultCounselorAndLetterStatusOngoingOrderByUpdatedAtDesc(
                    counselor);
        }

        int consultOffset = isCustomer ? CUSTOMER_ONGOING_CONSULT : COUNSELOR_ONGOING_CONSULT;
        List<ChatLetterGetResponse> chatLetterGetResponses = new ArrayList<>();
        for (int i = 0; i < consultOffset; i++) {
            try {
                Letter letter = letters.get(i);
                chatLetterGetResponses.add(LetterGetResponse.of(letter, getRecentMessage(letter), isCustomer));
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        return ChatLetterGetOngoingResponse.of(letters.size(), chatLetterGetResponses);
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
