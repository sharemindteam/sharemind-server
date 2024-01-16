package com.example.sharemind.email.exception;

import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {

    private final EmailErrorCode errorCode;

    public EmailException(EmailErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
