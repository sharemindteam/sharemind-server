package com.example.sharemind.payApp.application;

import jakarta.servlet.http.HttpServletRequest;

public interface PayAppService {

    String payConsult(Long paymentId);

    String payPost(Long postId);

    String confirmConsult(HttpServletRequest request);

    String confirmPost(HttpServletRequest request);
}
