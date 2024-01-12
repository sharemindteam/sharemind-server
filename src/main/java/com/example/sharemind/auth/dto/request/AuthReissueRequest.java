package com.example.sharemind.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthReissueRequest {

    @Schema(description = "refreshToken", example = "aksdghalskdjghlgksj")
    @NotBlank(message = "refresh token은 공백일 수 없습니다.")
    private String refreshToken;
}
