package com.example.sharemind.letterMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.example.sharemind.letterMessage.dto.request.*;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetDeadlineResponse;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetIsSavedResponse;
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
    private static final Integer DEADLINE_OFFSET = 1;

    private final LetterService letterService;
    private final LetterMessageRepository letterMessageRepository;

    @Transactional
    @Override
    public LetterMessage createLetterMessage(LetterMessageCreateRequest letterMessageCreateRequest, Customer customer) {
        Letter letter = letterService.getLetterByLetterId(
                letterMessageCreateRequest.getLetterId());
        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(
                letterMessageCreateRequest.getMessageType());

        letter.checkAuthority(messageType, customer);
        LetterMessage letterMessage = letterMessageRepository.save(letterMessageCreateRequest.toEntity(letter, messageType));

        letterMessage.updateLetterStatus();

        return letterMessage;
    }

    @Transactional
    @Override
    public LetterMessage updateLetterMessage(LetterMessageUpdateRequest letterMessageUpdateRequest, Customer customer) {
        LetterMessage letterMessage = letterMessageRepository.findByMessageIdAndIsActivatedIsTrue(
                letterMessageUpdateRequest.getMessageId())
                .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));
        if (letterMessage.getIsCompleted()) {
            throw new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_ALREADY_COMPLETED);
        }

        letterMessage.getLetter().checkAuthority(letterMessage.getMessageType(), customer);
        letterMessage.updateLetterMessage(letterMessageUpdateRequest.getContent(), letterMessageUpdateRequest.getIsCompleted());

        letterMessage.updateLetterStatus();

        return letterMessage;
    }

    @Transactional
    @Override
    public void createFirstQuestion(LetterMessageCreateFirstRequest letterMessageCreateFirstRequest, Customer customer) {
        ConsultCategory category = ConsultCategory.getConsultCategoryByName(letterMessageCreateFirstRequest.getConsultCategory());

        LetterMessage letterMessage = createLetterMessage(LetterMessageCreateRequest.of(letterMessageCreateFirstRequest), customer);
        letterMessage.updateConsultCategory(category);
    }

    @Transactional
    @Override
    public void updateFirstQuestion(LetterMessageUpdateFirstRequest letterMessageUpdateFirstRequest, Customer customer) {
        ConsultCategory category = ConsultCategory.getConsultCategoryByName(letterMessageUpdateFirstRequest.getConsultCategory());

        LetterMessage letterMessage = updateLetterMessage(LetterMessageUpdateRequest.of(letterMessageUpdateFirstRequest), customer);
        letterMessage.updateConsultCategory(category);
    }

    @Override
    public LetterMessageGetIsSavedResponse getIsSaved(LetterMessageGetIsSavedRequest letterMessageGetIsSavedRequest) {
        Letter letter = letterService.getLetterByLetterId(
                letterMessageGetIsSavedRequest.getLetterId());
        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(
                letterMessageGetIsSavedRequest.getMessageType());

        if (letterMessageRepository.existsByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(
                letter, messageType, IS_NOT_COMPLETED)) {
            LetterMessage letterMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(
                            letter, messageType, IS_NOT_COMPLETED)
                    .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));

            return LetterMessageGetIsSavedResponse.of(letterMessage);
        } else {
            return LetterMessageGetIsSavedResponse.of();
        }
    }

    @Override
    public LetterMessageGetResponse getLetterMessage(LetterMessageGetRequest letterMessageGetRequest) {
        Letter letter = letterService.getLetterByLetterId(
                letterMessageGetRequest.getLetterId());
        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(
                letterMessageGetRequest.getMessageType());

        if (letterMessageRepository.existsByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(
                letter, messageType, letterMessageGetRequest.getIsCompleted())) {
            LetterMessage letterMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedAndIsActivatedIsTrue(
                    letter, messageType, letterMessageGetRequest.getIsCompleted())
                    .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));

            return LetterMessageGetResponse.of(letterMessage);
        } else {
            return LetterMessageGetResponse.of();
        }
    }

    @Override
    public LetterMessageGetDeadlineResponse getDeadline(LetterMessageGetDeadlineRequest letterMessageGetDeadlineRequest) {
        Letter letter = letterService.getLetterByLetterId(
                letterMessageGetDeadlineRequest.getLetterId());
        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(
                letterMessageGetDeadlineRequest.getMessageType());

        LetterMessageType previousType = null;
        switch (messageType) {
            case FIRST_QUESTION -> throw new LetterMessageException(LetterMessageErrorCode.NO_DEADLINE);

            case FIRST_REPLY -> previousType = LetterMessageType.FIRST_QUESTION;

            case SECOND_QUESTION -> previousType = LetterMessageType.FIRST_REPLY;

            case SECOND_REPLY -> previousType = LetterMessageType.SECOND_QUESTION;
        }

        if (!letter.getLetterStatus().equals(previousType.getLetterStatus())) {
            throw new LetterMessageException(LetterMessageErrorCode.NO_DEADLINE);
        }

        return LetterMessageGetDeadlineResponse.of(letter.getUpdatedAt().plusDays(DEADLINE_OFFSET));
    }
}
