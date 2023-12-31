package com.example.sharemind.customer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomerErrorCode {

    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 회원으로 등록된 이메일입니다.");

    private final HttpStatus errorHttpStatus;
    private String errorMessage;

    CustomerErrorCode(HttpStatus errorHttpStatus, String errorMessage) {
        this.errorHttpStatus = errorHttpStatus;
        this.errorMessage = errorMessage;
    }

    public void updateErrorMessage(String wrongInput) {
        this.errorMessage = this.errorMessage + " : " + wrongInput;
    }
}
