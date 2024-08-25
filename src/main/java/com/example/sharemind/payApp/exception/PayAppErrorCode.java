package com.example.sharemind.payApp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PayAppErrorCode {

    ALREADY_REQUESTED_PAYMENT(HttpStatus.BAD_REQUEST, "결제 요청 정보가 이미 존재합니다."),
    PAY_RESPONSE_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 요청에 대한 응답이 존재하지 않습니다."),
    PAY_REQUEST_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "결제 요청이 실패하였습니다."),
    CONFIRM_BASIC_INFO_FAIL(HttpStatus.BAD_REQUEST, "판매자 아이디, key, value 값이 일치하지 않습니다."),
    CONFIRM_PAYMENT_INFO_FAIL(HttpStatus.BAD_REQUEST, "상담료, payAppId 값이 일치하지 않습니다."),
    METHOD_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 수단을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    PayAppErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
