package com.example.sharemind.counselor.exception;

import lombok.Getter;

@Getter
public class CounselorException extends RuntimeException {

    private final CounselorErrorCode errorCode;

    public CounselorException(CounselorErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CounselorException(CounselorErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
