package com.example.sharemind.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGetConnectResponse {

    @Schema(description = "유저 id, customer일 경우 customerId, counselor일 경우 counselorId")
    private final Long userId;

    @Schema(description = "현재 유저가 들어가있는 방번호")
    private final List<Long> roomIds;

    public static ChatGetConnectResponse of(Long userId, List<Long> roomIds) {
        return new ChatGetConnectResponse(userId, roomIds);
    }
}
