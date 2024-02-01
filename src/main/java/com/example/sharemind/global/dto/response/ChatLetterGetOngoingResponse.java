package com.example.sharemind.global.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatLetterGetOngoingResponse {
    private final Integer totalOngoing;
    private final List<ChatLetterGetResponse> consults;

    public static ChatLetterGetOngoingResponse of(Integer totalOngoing, List<ChatLetterGetResponse> consults) {
        return new ChatLetterGetOngoingResponse(totalOngoing, consults);
    }
}
