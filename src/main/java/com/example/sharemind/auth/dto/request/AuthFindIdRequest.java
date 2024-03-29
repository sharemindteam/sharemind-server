package com.example.sharemind.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthFindIdRequest {

    @Schema(description = "복구 이메일 주소", example = "abc@gmail.com")
    @NotBlank(message = "복구 이메일은 공백일 수 없습니다.")
    private String recoveryEmail;
}
