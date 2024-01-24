package com.example.sharemind.counselor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CounselorGetRequest {

    @Schema(description = "상담 카테고리", example = "연애갈등")
    private String consultCategory;

    @Schema(description = "페이지 번호 index 0이면 0~3번째 값 반환, 1이면 4~7번째 값 반환", example = "0")
    @NotNull
    private int index;
}
