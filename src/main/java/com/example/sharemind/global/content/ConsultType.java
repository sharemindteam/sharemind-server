package com.example.sharemind.global.content;

import com.example.sharemind.global.exception.GlobalErrorCode;
import com.example.sharemind.global.exception.GlobalException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ConsultType {
    CHAT("채팅"),
    LETTER("편지");

    private final String displayName;

    ConsultType(String displayName) {
        this.displayName = displayName;
    }

    public static ConsultType getConsultTypeByName(String name) {
        return Arrays.stream(ConsultType.values())
                .filter(consultType -> consultType.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new GlobalException(GlobalErrorCode.CONSULT_TYPE_NOT_FOUND, name));
    }
}
