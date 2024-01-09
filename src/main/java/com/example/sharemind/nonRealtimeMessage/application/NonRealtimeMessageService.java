package com.example.sharemind.nonRealtimeMessage.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.nonRealtimeMessage.dto.request.NonRealtimeMessageCreateRequest;

public interface NonRealtimeMessageService {
    void createNonRealtimeMessage(NonRealtimeMessageCreateRequest nonRealtimeMessageCreateRequest, Customer customer);
}
