package com.example.sharemind.chat.dto.response;

import com.example.sharemind.consult.domain.Consult;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGetStatusResponse {

    private final String customerNickname;
    private final String counselorNickname;
    private final LocalDateTime localDateTime;

    public static ChatGetStatusResponse of(Consult consult) {
        return new ChatGetStatusResponse(consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                LocalDateTime.now());
    }
}