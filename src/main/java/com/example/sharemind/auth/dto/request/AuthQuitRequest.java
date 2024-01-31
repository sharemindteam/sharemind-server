package com.example.sharemind.auth.dto.request;

import com.example.sharemind.customer.domain.Quit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthQuitRequest {

    @Schema(description = "탈퇴 사유", example = "사용을 잘 안 하게 돼요")
    @NotBlank(message = "탈퇴 사유는 공백일 수 없습니다.")
    private String shortReason;

    @Schema(description = "선택사항", example = "상담사가 너무 재수없어서 안 쓰고 싶어요")
    @Size(max = 100, message = "선택사항은 최대 100자입니다.")
    private String longReason;

    @Schema(description = "accessToken", example = "Bearer aasldkgjsalaksdjghlasdkjghslgjk")
    @NotBlank(message = "accessToken은 공백일 수 없습니다.")
    private String accessToken;

    @Schema(description = "refreshToken", example = "lakjsdlhsakjghslgkjdhslgkjdshaglk")
    @NotBlank(message = "refreshToken은 공백일 수 없습니다.")
    private String refreshToken;

    public Quit toEntity() {
        return Quit.builder()
                .shortReason(shortReason)
                .longReason(longReason)
                .build();
    }
}
