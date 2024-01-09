package com.example.sharemind.nonRealtimeMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.nonRealtimeMessage.content.LetterMessageType;
import com.example.sharemind.nonRealtimeMessage.dto.request.LetterMessageCreateRequest;
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
        // TODO 순서에 맞게(1차 질문-답변, 2차 질문-답변) 메시지 유형이 들어온 것인지 확인하는 로직도 필요

        letterMessageRepository.save(letterMessageCreateRequest.toEntity(letter, messageType));
    }
}
