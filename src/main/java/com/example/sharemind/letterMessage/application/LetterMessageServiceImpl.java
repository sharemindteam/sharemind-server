package com.example.sharemind.letterMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.example.sharemind.letterMessage.dto.request.LetterMessageCreateRequest;
import com.example.sharemind.letterMessage.dto.request.LetterMessageGetIsSavedRequest;
import com.example.sharemind.letterMessage.dto.request.LetterMessageUpdateRequest;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetIsSavedResponse;
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

    private final LetterService letterService;
    private final LetterMessageRepository letterMessageRepository;

    @Transactional
    @Override
    public void createLetterMessage(LetterMessageCreateRequest letterMessageCreateRequest, Customer customer) {
        Letter letter = letterService.getLetterByLetterId(
                letterMessageCreateRequest.getLetterId());
        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(
                letterMessageCreateRequest.getMessageType());

        letter.checkAuthority(messageType, customer);
        LetterMessage letterMessage = letterMessageRepository.save(letterMessageCreateRequest.toEntity(letter, messageType));

        letterMessage.updateLetterStatus();
    }

    @Transactional
    @Override
    public void updateLetterMessage(LetterMessageUpdateRequest letterMessageUpdateRequest, Customer customer) {
        LetterMessage letterMessage = letterMessageRepository.findByMessageIdAndIsActivatedIsTrue(
                letterMessageUpdateRequest.getMessageId())
                .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));
        if (letterMessage.getIsCompleted()) {
            throw new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_ALREADY_COMPLETED);
        }

        letterMessage.getLetter().checkAuthority(letterMessage.getMessageType(), customer);
        letterMessage.updateLetterMessage(letterMessageUpdateRequest.getContent(), letterMessageUpdateRequest.getIsCompleted());

        letterMessage.updateLetterStatus();
    }

    @Override
    public LetterMessageGetIsSavedResponse getIsSaved(LetterMessageGetIsSavedRequest letterMessageGetIsSavedRequest) {
        Letter letter = letterService.getLetterByLetterId(
                letterMessageGetIsSavedRequest.getLetterId());
        LetterMessageType messageType = LetterMessageType.getLetterMessageTypeByName(
                letterMessageGetIsSavedRequest.getMessageType());

        if (letterMessageRepository.existsByLetterAndMessageTypeAndIsCompletedIsFalseAndIsActivatedIsTrue(letter, messageType)) {
            LetterMessage letterMessage = letterMessageRepository.findByLetterAndMessageTypeAndIsCompletedIsFalseAndIsActivatedIsTrue(letter, messageType)
                    .orElseThrow(() -> new LetterMessageException(LetterMessageErrorCode.LETTER_MESSAGE_NOT_FOUND));

            return LetterMessageGetIsSavedResponse.of(letterMessage);
        } else {
            return LetterMessageGetIsSavedResponse.of();
        }
    }
}
