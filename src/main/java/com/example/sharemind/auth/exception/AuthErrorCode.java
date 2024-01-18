package com.example.sharemind.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode {

    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 회원으로 등록된 이메일입니다."),
    INVALID_RECOVERY_EMAIL(HttpStatus.BAD_REQUEST, "로그인 이메일과 동일한 이메일은 복구 이메일로 사용할 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "access token이 이미 만료되었거나 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh token이 이미 만료되었거나 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
