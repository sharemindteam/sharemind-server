package com.example.sharemind.auth.dto.request;

import com.example.sharemind.customer.domain.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.Random;

@Getter
public class AuthSignUpRequest {

    @Schema(description = "이메일", example = "abc@gmail.com")
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    private String email;

    @Schema(description = "비밀번호", example = "abcde12345")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp = "^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\\d~!@#$%^&*()_+=]{10,}$",
            message = "비밀번호는 영문, 숫자, 특수문자 중 2가지 이상을 조합한 10자리 이상의 문자열이어야 합니다.")
    private String password;

    @Schema(description = "전화번호(하이픈 필수)", example = "010-1234-5678")
    @NotBlank(message = "전화번호는 공백일 수 없습니다.")
    private String phoneNumber;

    @Schema(description = "복구 이메일", example = "bdskfj@gmail.com")
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
