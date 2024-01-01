package com.example.sharemind.auth.dto.request;

import com.example.sharemind.customer.domain.Customer;
import lombok.Getter;

import java.util.Random;

@Getter
public class AuthSignUpRequest {

    private String email;
    private String password;
    private String phoneNumber;
    private String recoveryEmail;

    public Customer toEntity(String password) {
        return Customer.builder()
                .nickname("사용자" + new Random().nextInt(999999))
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .recoveryEmail(recoveryEmail)
                .build();
    }
}
