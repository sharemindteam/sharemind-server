package com.example.sharemind.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode {

    ILLEGAL_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ILLEGAL_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh token이 이미 만료되었거나 올바르지 않습니다.");

    private final HttpStatus errorHttpStatus;
    private String errorMessage;

    AuthErrorCode(HttpStatus errorHttpStatus, String errorMessage) {
        this.errorHttpStatus = errorHttpStatus;
        this.errorMessage = errorMessage;
    }

    public void updateErrorMessage(String wrongInput) {
        this.errorMessage = this.errorMessage + " : " + wrongInput;
    }
}
