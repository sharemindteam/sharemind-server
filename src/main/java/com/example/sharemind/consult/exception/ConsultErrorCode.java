package com.example.sharemind.consult.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ConsultErrorCode {

    CONSULT_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 정보가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

     ConsultErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}