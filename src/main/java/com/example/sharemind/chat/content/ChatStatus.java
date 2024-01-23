package com.example.sharemind.chat.content;

import com.example.sharemind.chat.exception.ChatErrorCode;
import com.example.sharemind.chat.exception.ChatException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ChatStatus {
    WAITING("상담 대기"),
    ONGOING("상담 중"),
    SEND_REQUEST("상담 시작 요청"),
    FIVE_MINUTE_LEFT("상담 종료 5분 전"),
    TIME_OVER("시간 종료"),
    FINISH("상담 종료"),
    CANCEL("상담 취소");

    private final String displayName;

    ChatStatus(String displayName) {
        this.displayName = displayName;
    }

    public static ChatStatus getChatStatusByName(String name) {
        return Arrays.stream(ChatStatus.values())
                .filter(chatStatus -> chatStatus.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_STATUS_NOT_FOUND, name));
    }
}
