package com.example.sharemind.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomExceptionResponse {

    @Schema(description = "예외 이름", example = "CONSULT_TYPE_NOT_FOUND")
    private final String errorName;

    @Schema(description = "예외 메시지", example = "상담 유형이 존재하지 않습니다.")
    private final String message;

    @Schema(description = "발생 시각")
    private final LocalDateTime timeStamp;

    public static CustomExceptionResponse of(String errorName, String message) {
        return new CustomExceptionResponse(errorName, message, LocalDateTime.now());
    }
}
