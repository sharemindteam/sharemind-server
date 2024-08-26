package com.example.sharemind.sms.exception;

import lombok.Getter;

@Getter
public class SmsException extends RuntimeException {

    private final SmsErrorCode errorCode;

    public SmsException(SmsErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
