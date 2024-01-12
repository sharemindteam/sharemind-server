package com.example.sharemind.letter.dto.response;

import com.example.sharemind.letter.domain.Letter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetNicknameCategoryResponse {

    private final String nickname;
    private final String category;

    public static LetterGetNicknameCategoryResponse of(Letter letter) {
        return new LetterGetNicknameCategoryResponse(letter.getConsult().getCustomer().getNickname(),
                letter.getConsultCategory().getDisplayName());
    }
}
