package com.example.sharemind.chat.dto.request;

import com.example.sharemind.chat.content.ChatWebsocketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatStatusUpdateRequest {

    @NotNull(message = "채팅 상태는 공백일 수 없습니다.")
    private ChatWebsocketStatus chatWebsocketStatus;
}
