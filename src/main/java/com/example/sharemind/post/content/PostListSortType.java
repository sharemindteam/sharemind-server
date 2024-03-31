package com.example.sharemind.post.content;

import com.example.sharemind.post.exception.PostErrorCode;
import com.example.sharemind.post.exception.PostException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PostListSortType {

    LATEST("publishedAt"),
    DESC_TOTAL_COMMENT("totalComment"),
    DESC_TOTAL_LIKE("totalLike");

    private final String sortColumn;

    PostListSortType(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public static PostListSortType getSortTypeByName(String name) {
        return Arrays.stream(PostListSortType.values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(() -> new PostException(PostErrorCode.POST_SORT_TYPE_NOT_FOUND, name));
    }
}
