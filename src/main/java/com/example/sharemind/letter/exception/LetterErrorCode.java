package com.example.sharemind.letter.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LetterErrorCode {

    LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "편지(비실시간 상담) 정보가 존재하지 않습니다."),
    LETTER_SORT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "편지 정렬 방식이 존재하지 않습니다."),
    CONSULT_CATEGORY_MISMATCH(HttpStatus.BAD_REQUEST, "상담사가 제공하지 않은 상담 카테고리입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    LetterErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
