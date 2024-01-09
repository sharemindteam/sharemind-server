package com.example.sharemind.consult.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ConsultErrorCode {

    CONSULT_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 정보가 존재하지 않습니다."),
    CONSULT_TYPE_MISMATCH(HttpStatus.CONFLICT, "상담 유형과 일치하지 않습니다."),
    CONSULT_ALREADY_PAID(HttpStatus.BAD_REQUEST, "이미 결제 완료된 상담입니다.");

    private final HttpStatus httpStatus;
    private final String message;

     ConsultErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
