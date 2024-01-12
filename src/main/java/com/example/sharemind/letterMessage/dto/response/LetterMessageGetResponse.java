package com.example.sharemind.letterMessage.dto.response;

import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetResponse {

    private final Long messageId;
    private final String messageType;
    private final String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime updatedAt;

    public static LetterMessageGetResponse of(LetterMessage letterMessage) {
        return new LetterMessageGetResponse(letterMessage.getMessageId(), letterMessage.getMessageType().getDisplayName(),
                letterMessage.getContent(), letterMessage.getUpdatedAt());
    }

    public static LetterMessageGetResponse of() {
        return new LetterMessageGetResponse(null, null, null, null);
    }
}
