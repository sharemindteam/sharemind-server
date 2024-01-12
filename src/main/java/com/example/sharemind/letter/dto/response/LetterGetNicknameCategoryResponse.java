package com.example.sharemind.letter.dto.response;

import com.example.sharemind.letter.domain.Letter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetNicknameCategoryResponse {

    @Schema(description = "구매자 닉네임", example = "사용자1235")
    private final String nickname;

    @Schema(description = "구매자가 선택한 상담 카테고리", example = "이별/재회")
    private final String category;

    public static LetterGetNicknameCategoryResponse of(Letter letter) {
        return new LetterGetNicknameCategoryResponse(letter.getConsult().getCustomer().getNickname(),
                letter.getConsultCategory().getDisplayName());
    }
}
