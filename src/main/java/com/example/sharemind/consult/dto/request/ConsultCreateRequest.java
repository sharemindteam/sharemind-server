package com.example.sharemind.consult.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ConsultCreateRequest {

    @Schema(description = "상담사 아이디", example = "1")
    @NotNull(message = "상담사 id는 공백일 수 없습니다.")
    private Long counselorId;

    @Schema(description = "신청할 상담 종류", example = "Letter")
    @NotBlank(message = "상담 유형은 공백일 수 없습니다.")
    private String consultTypeName;
}
