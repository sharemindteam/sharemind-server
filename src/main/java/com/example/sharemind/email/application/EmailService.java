package com.example.sharemind.email.application;

import com.example.sharemind.email.content.EmailType;
import com.example.sharemind.email.dto.response.EmailGetSendCountResponse;

public interface EmailService {
    EmailGetSendCountResponse sendVerificationCode(String email);

    void verifyCode(String email, String code);

    void sendEmail(String to, EmailType emailType, String extraContent);
}
