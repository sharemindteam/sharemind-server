package com.example.sharemind.sms.application;

import com.example.sharemind.sms.dto.response.SmsGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    @Value("${spring.sms.key}")
    private String key;

    @Value("${spring.sms.id}")
    private String id;

    @Value("${spring.sms.sender}")
    private String sender;

    private final WebClient webClient;

    @Override
    public Mono<String> sendSms(String phoneNumber) {
        return webClient.post()
                .uri("/send/")
                .body(BodyInserters.fromFormData("key", key)
                        .with("user_id", id)
                        .with("sender", sender)
                        .with("receiver", "")
                        .with("msg", "테스트입니다")
//                        .with("testmode_yn", "Y")
                ).retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    System.out.println("Received response from API: " + response);
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.out.println("Error calling API: " + ex.getMessage());
                    return Mono.error(new RuntimeException("API 호출 실패: " + ex.getMessage()));
                });
    }
}
