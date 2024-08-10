package com.example.sharemind.counselor.content;

import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ConsultStyle {
    SYMPATHY("공감"),
    ADVICE("조언"),
    FACT("팩폭");

    private final String displayName;

    ConsultStyle(String displayName) {
        this.displayName = displayName;
    }

    public static ConsultStyle getConsultStyleByName(String name) {
        return Arrays.stream(ConsultStyle.values())
                .filter(consultStyle -> consultStyle.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new CounselorException(CounselorErrorCode.CONSULT_STYLE_NOT_FOUND, name));
    }
}
