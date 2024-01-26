package com.example.sharemind.payment.exception;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {

    private final PaymentErrorCode errorCode;

    public PaymentException(PaymentErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
