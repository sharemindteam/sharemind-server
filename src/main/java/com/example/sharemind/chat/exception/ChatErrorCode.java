package com.example.sharemind.chat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChatErrorCode {

    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅(실시간 상담) 정보가 존재하지 않습니다."),
    USER_NOT_IN_CHAT(HttpStatus.NOT_FOUND, "해당 유저가 채팅에 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ChatErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
