package com.example.sharemind.chat.content;

import lombok.Getter;

@Getter
public enum ChatWebsocketStatus {

    CHAT_CREATE,
    CHAT_START,
    CHAT_START_REQUEST_CANCEL,
    CHAT_LEFT_FIVE_MINUTE,
    CHAT_TIME_OVER,
    CHAT_FINISH,

    COUNSELOR_CHAT_START_REQUEST,
    CUSTOMER_CHAT_START_RESPONSE,
    CUSTOMER_CHAT_FINISH_REQUEST,
    CUSTOMER_CHAT_REVIEW,
}
