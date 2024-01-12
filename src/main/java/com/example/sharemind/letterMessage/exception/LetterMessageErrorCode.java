package com.example.sharemind.letterMessage.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LetterMessageErrorCode {

    LETTER_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지가 존재하지 않습니다."),
    LETTER_MESSAGE_ALREADY_CREATED(HttpStatus.BAD_REQUEST, "이미 메시지가 생성된 메시지 유형입니다."),
    LETTER_MESSAGE_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 메시지가 최종 제출되었습니다."),
    Letter_MESSAGE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지 유형이 존재하지 않습니다."),
    MESSAGE_MODIFY_DENIED(HttpStatus.FORBIDDEN, "메시지 작성 권한이 없습니다."),
    INVALID_LETTER_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "현재 작성 가능한 메시지 유형이 아닙니다."),
    NO_DEADLINE(HttpStatus.BAD_REQUEST, "마감시간이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    LetterMessageErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
