package com.example.sharemind.letterMessage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LetterMessageUpdateFirstRequest {

    @NotNull(message = "메시지 id는 공백일 수 없습니다.")
    private Long messageId;

    @NotBlank(message = "상담 카테고리는 공백일 수 없습니다.")
    private String consultCategory;

    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    private String content;

    @NotNull(message = "작성 완료 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;
}
