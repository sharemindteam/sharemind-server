package com.example.sharemind.customer.exception;

import lombok.Getter;

@Getter
public class CustomerException extends RuntimeException {

    private final CustomerErrorCode errorCode;

    public CustomerException(CustomerErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        errorCode.updateErrorMessage(message);
    }
}
