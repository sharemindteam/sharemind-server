package com.example.sharemind.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthSignOutRequest {

    @Schema(description = "accessToken", example = "Bearer aasldkgjsalaksdjghlasdkjghslgjk")
    @NotBlank(message = "accessToken은 공백일 수 없습니다.")
    private String accessToken;

    @Schema(description = "refreshToken", example = "lakjsdlhsakjghslgkjdhslgkjdshaglk")
    @NotBlank(message = "refreshToken은 공백일 수 없습니다.")
    private String refreshToken;

    public static AuthSignOutRequest of(String accessToken, String refreshToken) {
        return new AuthSignOutRequest(accessToken, refreshToken);
    }
}
