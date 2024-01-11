package com.example.sharemind.nonRealtimeMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.nonRealtimeMessage.content.LetterMessageType;
import com.example.sharemind.nonRealtimeMessage.domain.LetterMessage;
import com.example.sharemind.nonRealtimeMessage.dto.request.LetterMessageCreateRequest;
import com.example.sharemind.nonRealtimeMessage.dto.request.LetterMessageUpdateRequest;
import com.example.sharemind.nonRealtimeMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.nonRealtimeMessage.exception.LetterMessageException;
import com.example.sharemind.nonRealtimeMessage.repository.LetterMessageRepository;
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
}
