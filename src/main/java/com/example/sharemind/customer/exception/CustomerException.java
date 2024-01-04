package com.example.sharemind.customer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomerException extends RuntimeException {

    private final HttpStatus errorCode;

    public CustomerException(CustomerErrorCode errorCode, String message) {
        super(errorCode.getErrorMessage() + " : " + message);
        this.errorCode = errorCode.getErrorHttpStatus();
    }
}
