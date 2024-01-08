package com.example.sharemind.consult.exception;

import lombok.Getter;

@Getter
public class ConsultException extends RuntimeException {

    private final ConsultErrorCode errorCode;

    public ConsultException(ConsultErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ConsultException(ConsultErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
