package com.example.sharemind.payment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentErrorCode {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역이 존재하지 않습니다."),
    PAYMENT_CUSTOMER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "구매자 결제 내역 유형이 존재하지 않습니다."),
    INVALID_REFUND_COMPLETE(HttpStatus.BAD_REQUEST, "환불 완료로 상태를 변경할 수 있는 결제가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;

    PaymentErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
