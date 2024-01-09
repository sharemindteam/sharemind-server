package com.example.sharemind.nonRealtimeMessage.content;

import com.example.sharemind.nonRealtimeMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.nonRealtimeMessage.exception.LetterMessageException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LetterMessageType {

    FIRST_QUESTION("질문"),
    FIRST_REPLY("답장"),
    SECOND_QUESTION("추가 질문"),
    SECOND_REPLY("추가 답장");

    private final String displayName;

    LetterMessageType(String displayName) {
        this.displayName = displayName;
    }

    public static LetterMessageType getLetterMessageTypeByName(String name) {
        return Arrays.stream(LetterMessageType.values())
                .filter(messageType -> messageType.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(
                        () -> new LetterMessageException(
                                LetterMessageErrorCode.Letter_MESSAGE_TYPE_NOT_FOUND, name
                        )
                );
    }
}
