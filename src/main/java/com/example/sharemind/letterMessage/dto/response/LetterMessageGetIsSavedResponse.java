package com.example.sharemind.letterMessage.dto.response;

import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetIsSavedResponse {

    private final Boolean isSaved;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime updatedAt;

    public static LetterMessageGetIsSavedResponse of(LetterMessage letterMessage) {
        return new LetterMessageGetIsSavedResponse(true, letterMessage.getUpdatedAt());
    }

    public static LetterMessageGetIsSavedResponse of() {
        return new LetterMessageGetIsSavedResponse(false, null);
    }
}
