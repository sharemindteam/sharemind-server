package com.example.sharemind.customer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomerErrorCode {

    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CustomerErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
