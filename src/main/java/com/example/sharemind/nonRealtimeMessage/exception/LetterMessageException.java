package com.example.sharemind.nonRealtimeMessage.exception;

import lombok.Getter;

@Getter
public class LetterMessageException extends RuntimeException {

    private final LetterMessageErrorCode errorCode;

    public LetterMessageException(LetterMessageErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public LetterMessageException(LetterMessageErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
