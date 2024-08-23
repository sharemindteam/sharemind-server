package com.example.sharemind.payApp.exception;

import lombok.Getter;

@Getter
public class PayAppException extends RuntimeException {

    private final PayAppErrorCode errorCode;

    public PayAppException(PayAppErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public PayAppException(PayAppErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
