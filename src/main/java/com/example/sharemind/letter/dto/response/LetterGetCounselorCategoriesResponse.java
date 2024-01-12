package com.example.sharemind.letter.dto.response;

import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.letter.domain.Letter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetCounselorCategoriesResponse {

    private final List<String> categories;

    public static LetterGetCounselorCategoriesResponse of(Letter letter) {
        List<String> categories = new ArrayList<>();
        for (ConsultCategory category : letter.getConsult().getCounselor().getConsultCategories()) {
            categories.add(category.getDisplayName());
        }

        return new LetterGetCounselorCategoriesResponse(categories);
    }
}
