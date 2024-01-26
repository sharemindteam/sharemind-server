package com.example.sharemind.payment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentErrorCode {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역이 존재하지 않습니다."),
    PAYMENT_CUSTOMER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "구매자 결제 내역 유형이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    PaymentErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
