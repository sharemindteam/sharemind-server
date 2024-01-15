package com.example.sharemind.email.application;

import com.example.sharemind.email.exception.EmailErrorCode;
import com.example.sharemind.email.exception.EmailException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final int EXPIRATION_TIME = 5 * 60; //5분
    private static final String EMAIL_PREFIX = "email: ";

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String email) {
        checkExistingCode(email);
        String code = createVerificationCode();
        sendEmail(email, code);
        storeCodeInRedis(email, code);
    }

    @Override
    public void verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(EMAIL_PREFIX + email);
        if (!code.equals(storedCode)) {
            throw new EmailException(EmailErrorCode.CODE_MISMATCH, code);
        }
    }

    private void checkExistingCode(String email) {
        String existingCode = redisTemplate.opsForValue().get(EMAIL_PREFIX + email);
        if (existingCode != null) {
            throw new EmailException(EmailErrorCode.CODE_ALREADY_EXISTS, email);
        }
    }

    private void storeCodeInRedis(String email, String code) {
        redisTemplate.opsForValue().set(EMAIL_PREFIX + email, code, EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    private String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000; // 100000~999999 범위로 숫자 생성
        return String.valueOf(code);
    }

    private void sendEmail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("sharemind 회원 가입 인증 코드입니다.");
        message.setText("sharemind 회원 가입 인증 코드입니다.\n5분 내에 입력해주세요\n코드 : " + text);
        mailSender.send(message);
    }
}
