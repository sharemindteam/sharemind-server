package com.example.sharemind.chat.dto.response;

import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.example.sharemind.consult.domain.Consult;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGetStatusResponse {

    private final String customerNickname;

    private final String counselorNickname;

    private final ChatWebsocketStatus chatWebsocketStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime localDateTime;

    public static ChatGetStatusResponse of(Consult consult, ChatWebsocketStatus chatWebsocketStatus) {
        return new ChatGetStatusResponse(consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                chatWebsocketStatus, LocalDateTime.now());
    }
}
