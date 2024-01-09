package com.example.sharemind.letterMessage.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LetterMessageErrorCode {

    Letter_MESSAGE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지 유형이 존재하지 않습니다."),
    MESSAGE_MODIFY_DENIED(HttpStatus.FORBIDDEN, "메시지 작성 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    LetterMessageErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
