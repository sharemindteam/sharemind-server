package com.example.sharemind.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDto {

    @Schema(description = "accessToken", example = "Bearer aasldkgjsalaksdjghlasdkjghslgjk")
    private final String accessToken;

    @Schema(description = "refreshToken", example = "lakjsdlhsakjghslgkjdhslgkjdshaglk")
    private final String refreshToken;

    public static TokenDto of(String accessToken, String refreshToken) {
        return new TokenDto(accessToken, refreshToken);
    }
}
