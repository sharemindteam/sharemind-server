package com.example.sharemind.chat.dto.response;

import com.example.sharemind.chat.content.ChatRoomWebsocketStatus;
import com.example.sharemind.chat.domain.Chat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatNotifyEventResponse {

    @Schema(description = "채팅방 id")
    private final Long chatId;

    @Schema(description = "이벤트 발생 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime createTime;

    @Schema(description = "채팅 이벤트")
    private final ChatRoomWebsocketStatus chatRoomWebsocketStatus;

    @Schema(description = "상담사 채팅 스타일")
    private final String consultStyle;

    @Schema(description = "상대방 닉네임")
    private final String opponentNickname;

    public static ChatNotifyEventResponse of(Long chatId, ChatRoomWebsocketStatus chatRoomWebsocketStatus) {
        return new ChatNotifyEventResponse(chatId, LocalDateTime.now(), chatRoomWebsocketStatus, null, null);
    }

    public static ChatNotifyEventResponse of(Chat chat, ChatRoomWebsocketStatus chatRoomWebsocketStatus, Boolean isCustomer) {
        if (isCustomer)
            return new ChatNotifyEventResponse(chat.getChatId(), LocalDateTime.now(), chatRoomWebsocketStatus,
                    chat.getConsult().getCounselor().getConsultStyle().getDisplayName(), chat.getConsult().getCounselor().getNickname());
        else
            return new ChatNotifyEventResponse(chat.getChatId(), LocalDateTime.now(), chatRoomWebsocketStatus,
                    chat.getConsult().getCounselor().getConsultStyle().getDisplayName(), chat.getConsult().getCustomer().getNickname());
    }
}
