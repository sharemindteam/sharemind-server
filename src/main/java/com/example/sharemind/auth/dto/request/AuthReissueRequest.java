package com.example.sharemind.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthReissueRequest {

    @NotBlank(message = "refresh token은 공백일 수 없습니다.")
    private String refreshToken;
}
