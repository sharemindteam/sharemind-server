package com.example.sharemind.nonRealtimeMessage.content;

import com.example.sharemind.nonRealtimeMessage.exception.NonRealtimeMessageErrorCode;
import com.example.sharemind.nonRealtimeMessage.exception.NonRealtimeMessageException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NonRealtimeMessageType {

    FIRST_QUESTION("질문"),
    FIRST_REPLY("답장"),
    SECOND_QUESTION("추가 질문"),
    SECOND_REPLY("추가 답장");

    private final String displayName;

    NonRealtimeMessageType(String displayName) {
        this.displayName = displayName;
    }

    public static NonRealtimeMessageType getNonRealtimeMessageTypeByName(String name) {
        return Arrays.stream(NonRealtimeMessageType.values())
                .filter(messageType -> messageType.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(
                        () -> new NonRealtimeMessageException(
                                NonRealtimeMessageErrorCode.NON_REALTIME_MESSAGE_TYPE_NOT_FOUND, name
                        )
                );
    }
}
