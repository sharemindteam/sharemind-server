package com.example.sharemind.chat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChatErrorCode {

    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅(실시간 상담) 정보가 존재하지 않습니다."),
    CHAT_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅 상태가 존재하지 않습니다."),
    USER_NOT_IN_CHAT(HttpStatus.NOT_FOUND, "해당 유저가 채팅에 존재하지 않습니다."),
    INVALID_CHAT_ROLE_REQUEST(HttpStatus.BAD_REQUEST, "해당 역할로는 처리할 수 없는 요청입니다."),
    INVALID_CHAT_STATUS_REQUEST(HttpStatus.BAD_REQUEST, "해당 채팅 단계에서는 처리할 수 없는 요청입니다."),
    INVALID_CHAT_REQUEST(HttpStatus.BAD_REQUEST, "처리할 수 없는 요청입니다."),
    INVALID_CHAT_SORT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 정렬 기준입니다."),
    CHAT_STATUS_FINISH(HttpStatus.BAD_REQUEST, "종료된 채팅에는 메세지를 보낼 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ChatErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
