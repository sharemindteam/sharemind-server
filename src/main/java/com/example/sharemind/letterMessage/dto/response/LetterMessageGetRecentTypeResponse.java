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

    @Schema(description = "상담 취소 여부")
    private final Boolean isCanceled;

    public static LetterMessageGetRecentTypeResponse of(String recentType, Boolean isCanceled) {
        return new LetterMessageGetRecentTypeResponse(recentType, isCanceled);
    }
}
