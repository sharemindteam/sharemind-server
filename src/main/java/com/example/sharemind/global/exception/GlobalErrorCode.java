package com.example.sharemind.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalErrorCode {

    CONSULT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 유형이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    GlobalErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
