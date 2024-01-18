package com.example.sharemind.letter.dto.response;

import com.example.sharemind.letter.domain.Letter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetDeadlineResponse {

    @Schema(description = "마감일시", example = "2024년 1월 25일 9시", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH시")
    private final LocalDateTime deadline;

    public static LetterGetDeadlineResponse of(Letter letter) {
        return new LetterGetDeadlineResponse(letter.getDeadline());
    }
}
