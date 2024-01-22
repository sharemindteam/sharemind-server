package com.example.sharemind.global.content;

import com.example.sharemind.letter.exception.LetterErrorCode;
import com.example.sharemind.letter.exception.LetterException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ChatLetterSortType {

    LATEST,
    UNREAD;

    public static ChatLetterSortType getSortTypeByName(String name) {
        return Arrays.stream(ChatLetterSortType.values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new LetterException(LetterErrorCode.LETTER_SORT_TYPE_NOT_FOUND, name));
    }
}
