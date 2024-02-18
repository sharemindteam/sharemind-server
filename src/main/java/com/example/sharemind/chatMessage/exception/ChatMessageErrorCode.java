package com.example.sharemind.chatMessage.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChatMessageErrorCode {

    SEND_REQUEST_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅 요청 상태가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ChatMessageErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
