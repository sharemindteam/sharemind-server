package com.example.sharemind.letterMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetDeadlineResponse {

    @Schema(description = "마감일시", example = "2024년 1월 25일 9시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH시")
    private final LocalDateTime deadline;

    public static LetterMessageGetDeadlineResponse of(LocalDateTime deadline) {
        return new LetterMessageGetDeadlineResponse(deadline);
    }
}