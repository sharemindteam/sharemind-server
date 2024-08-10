package com.example.sharemind.chat.dto.response;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.global.utils.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGetRoomInfoResponse {

    @Schema(description = "대화 상대방 닉네임", example = "사용자37482")
    private final String opponentNickname;

    @Schema(description = "채팅 진행 상태")
    private final String status;

    @Schema(description = "채팅 요청 상태일 때, 남은 시간, 채팅 요청 상태 이외에는 null")
    private final String time;

    public static ChatGetRoomInfoResponse of(Chat chat) {
        if (chat.getChatStatus() == ChatStatus.SEND_REQUEST) {
            return new ChatGetRoomInfoResponse(chat.getConsult().getCustomer().getNickname(),
                    chat.getChatStatus().getDisplayName(),
                    TimeUtil.getChatSendRequestLeftTime(chat.getUpdatedAt()));
        }
        return new ChatGetRoomInfoResponse(chat.getConsult().getCustomer().getNickname(),
                chat.getChatStatus().getDisplayName(), null);
    }
}
