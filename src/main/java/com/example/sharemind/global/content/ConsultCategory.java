package com.example.sharemind.global.content;

import com.example.sharemind.global.exception.GlobalErrorCode;
import com.example.sharemind.global.exception.GlobalException;
import lombok.Getter;

import java.util.Arrays;

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

    public static ConsultCategory getConsultCategoryByName(String name) {
        return Arrays.stream(ConsultCategory.values())
                .filter(category -> category.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new GlobalException(GlobalErrorCode.CONSULT_CATEGORY_NOT_FOUND, name));
    }
}
