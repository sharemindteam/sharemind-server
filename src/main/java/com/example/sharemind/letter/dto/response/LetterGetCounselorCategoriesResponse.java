package com.example.sharemind.letter.dto.response;

import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.letter.domain.Letter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetCounselorCategoriesResponse {

    @Schema(description = "상담 카테고리", example = "이별/재회, 권태기, 기타")
    private final List<String> categories;

    public static LetterGetCounselorCategoriesResponse of(Letter letter) {
        List<String> categories = new ArrayList<>();
        for (ConsultCategory category : letter.getConsult().getCounselor().getConsultCategories()) {
            categories.add(category.getDisplayName());
        }

        return new LetterGetCounselorCategoriesResponse(categories);
    }
}
