package com.example.sharemind.letterMessage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LetterMessageCreateFirstRequest {

    @Schema(description = "편지 아이디", example = "1")
    @NotNull(message = "편지 id는 공백일 수 없습니다.")
    private Long letterId;

    @Schema(description = "선택한 상담 카테고리", example = "권태기")
    @NotBlank(message = "상담 카테고리는 공백일 수 없습니다.")
    private String consultCategory;

    @Schema(description = "메시지 내용", example = "안녕하세요 어쩌구저쩌구~")
    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    private String content;

    @Schema(description = "임시저장/최종 제출 여부", example = "true")
    @NotNull(message = "작성 완료 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;
}
