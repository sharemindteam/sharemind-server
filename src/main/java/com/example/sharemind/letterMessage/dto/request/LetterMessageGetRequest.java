package com.example.sharemind.letterMessage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LetterMessageGetRequest {

    @NotNull(message = "편지 id는 공백일 수 없습니다.")
    private Long letterId;

    @NotBlank(message = "메시지 유형은 공백일 수 없습니다.")
    private String messageType;

    @NotNull(message = "임시저장 메시지 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;
}
