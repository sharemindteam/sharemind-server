package com.example.sharemind.nonRealtimeMessage.exception;

import lombok.Getter;

@Getter
public class NonRealtimeMessageException extends RuntimeException {

    private final NonRealtimeMessageErrorCode errorCode;

    public NonRealtimeMessageException(NonRealtimeMessageErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
