package com.example.sharemind.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalErrorCode {

    CONSULT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 카테고리가 존재하지 않습니다."),
    CONSULT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 유형이 존재하지 않습니다."),
    CONSULT_SORT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, " 해당 정렬 방식이 존재하지 않습니다."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "잘못된 아이디입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    GlobalErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
