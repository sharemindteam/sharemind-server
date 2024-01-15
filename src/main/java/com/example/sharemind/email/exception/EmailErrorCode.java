package com.example.sharemind.email.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EmailErrorCode {

    CODE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "아직 만료되지 않은 코드가 남아있습니다."),
    CODE_MISMATCH(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다."),
    INVALID_EMAIL(HttpStatus.FORBIDDEN, "잘못된 이메일 형식입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    EmailErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
