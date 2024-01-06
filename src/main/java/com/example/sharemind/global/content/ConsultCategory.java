package com.example.sharemind.global.content;

import lombok.Getter;

@Getter
public enum ConsultCategory {
    DATING("연애갈등"),
    BREAKUP("이별/재회"),
    FEMALE_PSYCHOLOGY("여자심리"),
    MALE_PSYCHOLOGY("남자심리"),
    BEGINNING("썸/연애시작"),
    ONE_SIDED("짝사랑"),
    BOREDOM("권태기"),
    ETC("기타");

    private final String displayName;

    ConsultCategory(String displayName) {
        this.displayName = displayName;
    }
}
