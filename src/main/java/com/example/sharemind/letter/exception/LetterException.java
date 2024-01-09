package com.example.sharemind.letter.exception;

import lombok.Getter;

@Getter
public class LetterException extends RuntimeException {

    private final LetterErrorCode errorCode;

    public LetterException(LetterErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
