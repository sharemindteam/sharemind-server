package com.example.sharemind.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthSignInRequest {

    @Schema(description = "로그인할 이메일 주소", example = "abc@gmail.com")
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    private String email;

    @Schema(description = "비밀번호", example = "abcde12345")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;
}
