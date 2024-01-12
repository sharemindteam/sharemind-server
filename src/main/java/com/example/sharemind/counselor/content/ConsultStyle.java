package com.example.sharemind.counselor.content;

import lombok.Getter;

@Getter
public enum ConsultStyle {
    SYMPATHY("공감"),
    ADVICE("조언"),
    FACT("팩폭");

    private final String displayName;

    ConsultStyle(String displayName) {
        this.displayName = displayName;
    }
}
