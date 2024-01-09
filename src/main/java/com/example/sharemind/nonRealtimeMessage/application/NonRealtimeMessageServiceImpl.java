package com.example.sharemind.nonRealtimeMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.nonRealtimeConsult.application.NonRealtimeConsultService;
import com.example.sharemind.nonRealtimeConsult.domain.NonRealtimeConsult;
import com.example.sharemind.nonRealtimeMessage.content.NonRealtimeMessageType;
import com.example.sharemind.nonRealtimeMessage.dto.request.NonRealtimeMessageCreateRequest;
import com.example.sharemind.nonRealtimeMessage.repository.NonRealtimeMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NonRealtimeMessageServiceImpl implements NonRealtimeMessageService {

    private final NonRealtimeConsultService nonRealtimeConsultService;
    private final NonRealtimeMessageRepository nonRealtimeMessageRepository;

    @Transactional
    @Override
    public void createNonRealtimeMessage(NonRealtimeMessageCreateRequest nonRealtimeMessageCreateRequest, Customer customer) {
        NonRealtimeConsult nonRealtimeConsult = nonRealtimeConsultService.getNonRealtimeConsultByNonReatimeConsultId(
                nonRealtimeMessageCreateRequest.getNonRealtimeConsultId());
        NonRealtimeMessageType messageType = NonRealtimeMessageType.getNonRealtimeMessageTypeByName(
                nonRealtimeMessageCreateRequest.getMessageType());

        nonRealtimeConsult.checkAuthority(messageType, customer);
        // TODO 순서에 맞게(1차 질문-답변, 2차 질문-답변) 메시지 유형이 들어온 것인지 확인하는 로직도 필요

        nonRealtimeMessageRepository.save(nonRealtimeMessageCreateRequest.toEntity(nonRealtimeConsult, messageType));
    }
}
