package com.example.sharemind.chat.dto.response;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.domain.Chat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGetRoomInfoResponse {

    @Schema(description = "대화 상대방 닉네임", example = "사용자37482")
    private final String opponentNickname;

    @Schema(description = "채팅 진행 상태")
    private final String status;

    public static ChatGetRoomInfoResponse of(Chat chat) {
        return new ChatGetRoomInfoResponse(chat.getConsult().getCustomer().getNickname(), chat.getChatStatus().getDisplayName());
    }
}
