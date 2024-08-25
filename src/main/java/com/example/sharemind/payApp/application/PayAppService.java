package com.example.sharemind.payApp.application;

import com.example.sharemind.payApp.dto.request.ConfirmPayRequest;

public interface PayAppService {

    String payConsult(Long paymentId);

    String payPost(Long postId);

    String confirmConsult(String userId, String key, String value, Long cost,
            String approvedAt, Integer method, Integer state, Long val1, String payAppId);

    String confirmPost(String userId, String key, String value, Long cost,
            String approvedAt, Integer method, Integer state, Long val1, String payAppId);

    String confirmConsult(ConfirmPayRequest confirmPayRequest);

    String confirmPost(ConfirmPayRequest confirmPayRequest);
}
