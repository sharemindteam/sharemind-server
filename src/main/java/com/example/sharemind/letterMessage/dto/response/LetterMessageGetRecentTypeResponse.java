package com.example.sharemind.letterMessage.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetRecentTypeResponse {

    private final String recentType;

    public static LetterMessageGetRecentTypeResponse of(String recentType) {
        return new LetterMessageGetRecentTypeResponse(recentType);
    }
}
