package com.example.sharemind.letterMessage.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.example.sharemind.letterMessage.dto.request.*;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetIsSavedResponse;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetRecentTypeResponse;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetResponse;
import com.example.sharemind.letterMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.letterMessage.exception.LetterMessageException;
import com.example.sharemind.letterMessage.repository.LetterMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterMessageServiceImpl implements LetterMessageService {

    private static final Boolean IS_NOT_COMPLETED = false;

    private final CustomerService customerService;
    private final LetterService letterService;
    private final LetterMessageRepository letterMessageRepository;

    @Transactional
    @Override
    public LetterMessage createLetterMessage(LetterMessageCreateRequest letterMessageCreateRequest,
            Customer customer) {
        Letter letter = letterService.getLetterByLetterId(
                letterMessageCreateRequest.getLetterId());
        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(
                letterMessageCreateRequest.getMessageType());
        if (letterMessageRepository.existsByLetterAndMessageTypeAndIsActivatedIsTrue(letter,
                messageType)) {
            throw new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_ALREADY_CREATED);
        }

        letter.checkWriteAuthority(messageType, customer);
        LetterMessage letterMessage = letterMessageRepository.save(
                letterMessageCreateRequest.toEntity(letter, messageType));

        letterMessage.updateLetterStatus();

        return letterMessage;
    }

    @Transactional
    @Override
    public LetterMessage updateLetterMessage(LetterMessageUpdateRequest letterMessageUpdateRequest,
            Customer customer) {
        LetterMessage letterMessage = letterMessageRepository.findByMessageIdAndIsActivatedIsTrue(
                        letterMessageUpdateRequest.getMessageId())
                .orElseThrow(() -> new LetterMessageException(
                        LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));
        if (letterMessage.getIsCompleted()) {
            throw new LetterMessageException(
                    LetterMessageErrorCode.LETTER_MESSAGE_ALREADY_COMPLETED);
        }

        letterMessage.getLetter().checkWriteAuthority(letterMessage.getMessageType(), customer);
        letterMessage.updateLetterMessage(letterMessageUpdateRequest.getContent(),
                letterMessageUpdateRequest.getIsCompleted());

        letterMessage.updateLetterStatus();

        return letterMessage;
    }

    @Transactional
    @Override
    public void createFirstQuestion(LetterMessageCreateFirstRequest letterMessageCreateFirstRequest,
            Customer customer) {
        ConsultCategory category = ConsultCategory.getConsultCategoryByName(
                letterMessageCreateFirstRequest.getConsultCategory());

        LetterMessage letterMessage = createLetterMessage(
                LetterMessageCreateRequest.of(letterMessageCreateFirstRequest), customer);
        letterMessage.updateConsultCategory(category);
    }

    @Transactional
    @Override
    public void updateFirstQuestion(LetterMessageUpdateFirstRequest letterMessageUpdateFirstRequest,
            Customer customer) {
        ConsultCategory category = ConsultCategory.getConsultCategoryByName(
                letterMessageUpdateFirstRequest.getConsultCategory());

        LetterMessage letterMessage = updateLetterMessage(
                LetterMessageUpdateRequest.of(letterMessageUpdateFirstRequest), customer);
        letterMessage.updateConsultCategory(category);
    }

    @Override
    public LetterMessageGetIsSavedResponse getIsSaved(Long letterId, String type, Long customerId) {
        Letter letter = letterService.getLetterByLetterId(letterId);
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        letter.checkReadAuthority(customer);

        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(type);

        if (letterMessageRepository.existsByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(
                letter, messageType, IS_NOT_COMPLETED)) {
            LetterMessage letterMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(
                            letter, messageType, IS_NOT_COMPLETED)
                    .orElseThrow(() -> new LetterMessageException(
                            LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));

            return LetterMessageGetIsSavedResponse.of(letterMessage);
        } else {
            return LetterMessageGetIsSavedResponse.of();
        }
    }

    @Transactional
    @Override
    public LetterMessageGetResponse getLetterMessage(Long letterId, String type,
            Boolean isCompleted, Customer customer) {
        Letter letter = letterService.getLetterByLetterId(letterId);
        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(type);

        Boolean isCustomer = letter.checkReadAuthority(customer);

        if (letterMessageRepository.existsByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(
                letter, messageType, isCompleted)) {
            LetterMessage letterMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(
                            letter, messageType, isCompleted)
                    .orElseThrow(() -> new LetterMessageException(
                            LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));

            if (isCompleted) {
                if (isCustomer) {
                    letter.updateCustomerReadId(letterMessage.getMessageId());
                } else {
                    letter.updateCounselorReadId(letterMessage.getMessageId());
                }
            }

            return LetterMessageGetResponse.of(letterMessage);
        } else {
            return LetterMessageGetResponse.of();
        }
    }

    @Override
    public LetterMessageGetRecentTypeResponse getRecentMessageType(Long letterId) {
        Letter letter = letterService.getLetterByLetterId(letterId);

        String recentType = null;
        switch (letterMessageRepository.countByLetterAndIsCompletedIsTrueAndIsActivatedIsTrue(
                letter)) {
            case 0 -> recentType = "해당 편지에 대해 작성된 메시지가 없습니다.";
            case 1 -> recentType = LetterMessageType.FIRST_QUESTION.getDisplayName();
            case 2 -> recentType = LetterMessageType.FIRST_REPLY.getDisplayName();
            case 3 -> recentType = LetterMessageType.SECOND_QUESTION.getDisplayName();
            case 4 -> recentType = LetterMessageType.SECOND_REPLY.getDisplayName();
        }

        Boolean isCanceled = letter.getLetterStatus().equals(LetterStatus.COUNSELOR_CANCEL) ||
                letter.getLetterStatus().equals(LetterStatus.CUSTOMER_CANCEL);

        return LetterMessageGetRecentTypeResponse.of(recentType, isCanceled);
    }
}
