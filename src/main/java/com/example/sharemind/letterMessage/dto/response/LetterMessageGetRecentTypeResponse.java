package com.example.sharemind.letterMessage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetRecentTypeResponse {

    @Schema(description = "마지막으로 작성된 메시지 종류", example = "추가 질문")
    private final String recentType;

    public static LetterMessageGetRecentTypeResponse of(String recentType) {
        return new LetterMessageGetRecentTypeResponse(recentType);
    }
}
