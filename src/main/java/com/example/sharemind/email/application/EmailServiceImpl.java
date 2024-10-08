package com.example.sharemind.email.application;

import com.example.sharemind.email.content.EmailType;
import com.example.sharemind.email.dto.response.EmailGetSendCountResponse;
import com.example.sharemind.email.exception.EmailErrorCode;
import com.example.sharemind.email.exception.EmailException;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final int EXPIRATION_TIME = 5;
    private static final int EMAIL_REQUEST_MAX_COUNT = 5;
    private static final String EMAIL_PREFIX = "email: ";

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Integer> countRedisTemplate;
    private final JavaMailSender mailSender;

    @Override
    public EmailGetSendCountResponse sendVerificationCode(String email) {
        String code = checkExistingCode(email);
        int count = 0;
        if (code == null) {
            code = createVerificationCode();
        } else {
            count = checkCodeCount(email);
        }
        sendEmail(email, EmailType.SIGNUP_CODE, code);
        storeCodeInRedis(email, code, count);

        return EmailGetSendCountResponse.of(count + 1);
    }

    @Override
    public void verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(EMAIL_PREFIX + email);
        if (!code.equals(storedCode)) {
            throw new EmailException(EmailErrorCode.CODE_MISMATCH, code);
        }
    }

    @Async
    @Override
    public void sendEmail(String to, EmailType emailType, String extraContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(emailType.getSubject());
        message.setText(emailType.getText() + extraContent);

        mailSender.send(message);
    }

    private String checkExistingCode(String email) {
        return redisTemplate.opsForValue().get(EMAIL_PREFIX + email);
    }

    private int checkCodeCount(String email) {
        Integer count = countRedisTemplate.opsForValue().get(EMAIL_PREFIX + email);
        if ((count == null) || (count >= EMAIL_REQUEST_MAX_COUNT || count <= 0)) {
            throw new EmailException(EmailErrorCode.CODE_REQUEST_COUNT_EXCEED, email);
        }
        return count;
    }

    private void storeCodeInRedis(String email, String code, int count) {
        if (count == 0) {
            redisTemplate.opsForValue()
                    .set(EMAIL_PREFIX + email, code, EXPIRATION_TIME, TimeUnit.MINUTES);
            countRedisTemplate.opsForValue()
                    .set(EMAIL_PREFIX + email, 1, EXPIRATION_TIME, TimeUnit.MINUTES);
        } else {
            countRedisTemplate.opsForValue().set(EMAIL_PREFIX + email, count + 1);
        }
    }

    private String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000; // 100000~999999 범위로 숫자 생성
        return String.valueOf(code);
    }
}
