package com.example.sharemind.email.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EmailErrorCode {

    CODE_MISMATCH(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다."),
    CODE_REQUEST_COUNT_EXCEED(HttpStatus.BAD_REQUEST, "해당 이메일의 코드 요청 횟수가 초과되었습니다. 5분 후에 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String message;

    EmailErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
