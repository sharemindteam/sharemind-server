package com.example.sharemind.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthGetPasswordMatchRequest {

    @Schema(description = "현재 비밀번호")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;
}
