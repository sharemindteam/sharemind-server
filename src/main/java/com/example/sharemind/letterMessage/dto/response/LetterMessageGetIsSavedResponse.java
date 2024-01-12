package com.example.sharemind.letterMessage.dto.response;

import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetIsSavedResponse {

    @Schema(description = "임시저장 메시지 존재하면 true, 아니면 false", example = "true")
    private final Boolean isSaved;

    @Schema(description = "마지막 수정일시, isSaved false면 null", example = "2023년 12월 25일 오후 12시 34분")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime updatedAt;

    public static LetterMessageGetIsSavedResponse of(LetterMessage letterMessage) {
        return new LetterMessageGetIsSavedResponse(true, letterMessage.getUpdatedAt());
    }

    public static LetterMessageGetIsSavedResponse of() {
        return new LetterMessageGetIsSavedResponse(false, null);
    }
}
