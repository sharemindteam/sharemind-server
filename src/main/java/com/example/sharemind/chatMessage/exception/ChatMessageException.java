package com.example.sharemind.chatMessage.exception;

import lombok.Getter;

@Getter
public class ChatMessageException extends RuntimeException {

    private final ChatMessageErrorCode errorCode;

    public ChatMessageException(ChatMessageErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
