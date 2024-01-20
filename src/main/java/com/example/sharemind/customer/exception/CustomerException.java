package com.example.sharemind.customer.exception;

import lombok.Getter;

@Getter
public class CustomerException extends RuntimeException {

    private final CustomerErrorCode errorCode;

    public CustomerException(CustomerErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomerException(CustomerErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
