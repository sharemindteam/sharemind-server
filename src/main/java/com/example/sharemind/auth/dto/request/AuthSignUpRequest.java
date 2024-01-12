package com.example.sharemind.auth.dto.request;

import com.example.sharemind.customer.domain.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.Random;

@Getter
public class AuthSignUpRequest {

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp = "^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\\d~!@#$%^&*()_+=]{10,}$",
            message = "비밀번호는 영문, 숫자, 특수문자 중 2가지 이상을 조합한 10자리 이상의 문자열이어야 합니다.")
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
