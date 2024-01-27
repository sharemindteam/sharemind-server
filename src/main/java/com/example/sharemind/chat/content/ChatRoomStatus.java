package com.example.sharemind.chat.content;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum ChatRoomStatus {

    @Schema(description = "채팅 생성")
    CHAT_ROOM_CREATE,

    @Schema(description = "채팅 끝")
    CHAT_ROOM_FINISH
}
