package com.example.sharemind.customer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomerErrorCode {

    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 회원으로 등록된 이메일입니다."),
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CustomerErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
