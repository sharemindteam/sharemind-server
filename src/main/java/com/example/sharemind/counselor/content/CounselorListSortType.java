package com.example.sharemind.counselor.content;

import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum CounselorListSortType {

    LATEST("profileUpdatedAt"),
    POPULARITY("totalConsult"),
    STAR_RATING("ratingAverage");

    private final String sortColumn;

    CounselorListSortType(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public static CounselorListSortType getSortTypeByName(String name) {
        return Arrays.stream(CounselorListSortType.values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(() -> new CounselorException(CounselorErrorCode.COUNSELOR_SORT_TYPE_NOT_FOUND, name));
    }
}
