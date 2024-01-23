package com.example.sharemind.global.content;

import com.example.sharemind.global.exception.GlobalErrorCode;
import com.example.sharemind.global.exception.GlobalException;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ChatLetterSortType {

    LATEST,
    UNREAD;

    public static ChatLetterSortType getSortTypeByName(String name) {
        return Arrays.stream(ChatLetterSortType.values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new GlobalException(GlobalErrorCode.CONSULT_SORT_TYPE_NOT_FOUND, name));
    }
}
