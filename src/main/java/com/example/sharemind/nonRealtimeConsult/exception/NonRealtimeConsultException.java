package com.example.sharemind.nonRealtimeConsult.exception;

import lombok.Getter;

@Getter
public class NonRealtimeConsultException extends RuntimeException {

    private final NonRealtimeConsultErrorCode errorCode;

    public NonRealtimeConsultException(NonRealtimeConsultErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
