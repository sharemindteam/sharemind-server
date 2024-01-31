package com.example.sharemind.letter.dto.response;

import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetOngoingResponse {
    private final Integer totalOngoing;
    private final List<ChatLetterGetResponse> letters;

    public static LetterGetOngoingResponse of(Integer totalOngoing, List<ChatLetterGetResponse> letters) {
        return new LetterGetOngoingResponse(totalOngoing, letters);
    }
}
