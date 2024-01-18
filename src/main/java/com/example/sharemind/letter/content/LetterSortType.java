package com.example.sharemind.letter.content;

import com.example.sharemind.letter.exception.LetterErrorCode;
import com.example.sharemind.letter.exception.LetterException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LetterSortType {

    LATEST,
    UNREAD;

    public static LetterSortType getSortTypeByName(String name) {
        return Arrays.stream(LetterSortType.values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(() -> new LetterException(LetterErrorCode.LETTER_SORT_TYPE_NOT_FOUND, name));
    }
}
