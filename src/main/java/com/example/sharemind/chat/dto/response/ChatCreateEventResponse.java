package com.example.sharemind.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatCreateEventResponse {

    @Schema(description = "채팅방 id")
    private final Long chatId;

    @Schema(description = "채팅방 생성 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime createTime;

    public static ChatCreateEventResponse of(Long chatId) {
        return new ChatCreateEventResponse(chatId, LocalDateTime.now());
    }
}
