package com.example.sharemind.chat.dto.response;

import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.example.sharemind.consult.domain.Consult;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGetStatusResponse {

    @Schema(description = "고객 닉네임")
    private final String customerNickname;

    @Schema(description = "상담사 닉네임")
    private final String counselorNickname;

    @Schema(description = "채팅 웹소켓 상태")
    private final ChatWebsocketStatus chatWebsocketStatus;

    @Schema(description = "상태 업데이트 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime localDateTime;

    public static ChatGetStatusResponse of(Consult consult, ChatWebsocketStatus chatWebsocketStatus) {
        return new ChatGetStatusResponse(consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                chatWebsocketStatus, LocalDateTime.now());
    }
}
