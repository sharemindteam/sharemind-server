package com.example.sharemind.counselor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class CounselorUpdateProfileRequest {

    @Schema(description = "닉네임")
    @Size(min = 1, max = 8, message = "닉네임은 최대 8자입니다.")
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    private String nickname;

    @Schema(description = "상담 카테고리", example = "[\"DATING\", \"One_SiDed\", \"male_psychology\"]")
    @NotEmpty(message = "상담 카테고리는 공백일 수 없습니다.")
    private List<String> consultCategories;

    @Schema(description = "상담 스타일", example = "advice")
    @NotBlank(message = "상담 스타일은 공백일 수 없습니다.")
    private String consultStyle;

    @Schema(description = "상담 방식", example = "[\"letter\", \"chat\"]")
    @NotEmpty(message = "상담 방식은 공백일 수 없습니다.")
    private List<String> consultTypes;

    @Schema(description = "상담 가능시간", example = "{\"MON\": [\"14~15\", \"15~20\"], \"WED\": [\"11~13\"]}")
    private Map<String, List<String>> consultTimes;

    @Schema(description = "편지 상담료")
    @Min(value = 3000, message = "편지 상담료는 최소 3000원입니다.")
    @Max(value = 50000, message = "편지 상담료는 최대 50000원입니다.")
    private Long letterCost;

    @Schema(description = "채팅 상담료")
    @Min(value = 5000, message = "편지 상담료는 최소 5000원입니다.")
    @Max(value = 50000, message = "편지 상담료는 최대 50000원입니다.")
    private Long chatCost;

    @Schema(description = "한줄 소개")
    @Size(max = 50, message = "한줄 소개는 최대 50자입니다.")
    @NotBlank(message = "한줄 소개는 공백일 수 없습니다.")
    private String introduction;

    @Schema(description = "경험 소개")
    @Size(max = 20000, message = "경험 소개는 최대 20000자입니다.")
    @NotBlank(message = "경험 소개는 공백일 수 없습니다.")
    private String experience;
}
