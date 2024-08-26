package com.example.sharemind.sms.application;

import com.example.sharemind.sms.dto.response.SmsGetResponse;
import reactor.core.publisher.Mono;

public interface SmsService {

    public Mono<String> sendSms(String phoneNumber);
}
