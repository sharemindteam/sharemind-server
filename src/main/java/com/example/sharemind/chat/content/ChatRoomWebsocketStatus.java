package com.example.sharemind.chat.content;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum ChatRoomWebsocketStatus {

    @Schema(description = "채팅 생성")
    CHAT_ROOM_CREATE,

    @Schema(description = "채팅 끝")
    CHAT_ROOM_FINISH,

    @Schema(description = "채팅 unread 메세지 0으로(전부 읽음) 갱신")
    CHAT_READ_ALL
}
