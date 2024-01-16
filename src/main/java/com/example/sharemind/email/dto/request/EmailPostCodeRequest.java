package com.example.sharemind.email.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EmailPostCodeRequest {

    @NotNull(message = "이메일은 공백일 수 없습니다.")
    @Email
    private String email;

    @NotNull(message = "인증 코드는 공백일 수 없습니다.")
    private String code;
}
