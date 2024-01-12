package com.example.sharemind.letterMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetDeadlineResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 HH시")
    private final LocalDateTime deadline;

    public static LetterMessageGetDeadlineResponse of(LocalDateTime deadline) {
        return new LetterMessageGetDeadlineResponse(deadline);
    }
}
