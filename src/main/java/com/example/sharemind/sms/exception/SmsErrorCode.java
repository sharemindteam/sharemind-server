package com.example.sharemind.sms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SmsErrorCode {

    SEND_FAIL(HttpStatus.BAD_REQUEST, "전송 실패");
    
    private final HttpStatus httpStatus;
    private final String message;

    SmsErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
