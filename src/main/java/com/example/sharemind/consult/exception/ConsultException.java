package com.example.sharemind.consult.exception;

import lombok.Getter;

@Getter
public class ConsultException extends RuntimeException {

    private final ConsultErrorCode errorCode;

    public ConsultException(ConsultErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
