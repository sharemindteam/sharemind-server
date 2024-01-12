package com.example.sharemind.letterMessage.content;

import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.letterMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.letterMessage.exception.LetterMessageException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LetterMessageType {

    FIRST_QUESTION("질문", LetterStatus.FIRST_ASKING),
    FIRST_REPLY("답장", LetterStatus.FIRST_ANSWER),
    SECOND_QUESTION("추가 질문", LetterStatus.SECOND_ASKING),
    SECOND_REPLY("추가 답장", LetterStatus.FINISH);

    private final String displayName;
    private final LetterStatus letterStatus;

    LetterMessageType(String displayName, LetterStatus letterStatus) {
        this.displayName = displayName;
        this.letterStatus = letterStatus;
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
