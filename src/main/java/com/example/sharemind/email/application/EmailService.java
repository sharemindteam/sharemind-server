package com.example.sharemind.email.application;

public interface EmailService {
    void sendVerificationCode(String email);

    void verifyCode(String email, String code);
}
