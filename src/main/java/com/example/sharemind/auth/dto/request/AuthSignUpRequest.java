package com.example.sharemind.auth.dto.request;

import com.example.sharemind.customer.domain.Customer;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Random;

@Getter
public class AuthSignUpRequest {

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;

    @NotBlank(message = "전화번호는 공백일 수 없습니다.")
    private String phoneNumber;

    @NotBlank(message = "복구 이메일은 공백일 수 없습니다.")
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
