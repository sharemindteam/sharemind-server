package com.example.sharemind.letterMessage.dto.response;

import com.example.sharemind.letterMessage.domain.LetterMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetResponse {

    private final String messageType;
    private final String content;

    public static LetterMessageGetResponse of(LetterMessage letterMessage) {
        return new LetterMessageGetResponse(letterMessage.getMessageType().getDisplayName(),
                letterMessage.getContent());
    }

    public static LetterMessageGetResponse of() {
        return new LetterMessageGetResponse(null, null);
    }
}
