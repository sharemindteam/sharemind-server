package com.example.sharemind.chatMessage.content;

import lombok.Getter;

@Getter
public enum MessageStatus {
    MESSAGE("채팅 메세지"),
    SEND_REQUEST("상담 시작 요청"),
    START("상담 시작"),
    FIVE_MINUTE_LEFT("상담 종료 5분 전"),
    TIME_OVER("시간 종료"),
    FINISH("상담 종료");

    private final String displayName;

    MessageStatus(String displayName) {
        this.displayName = displayName;
    }
}
