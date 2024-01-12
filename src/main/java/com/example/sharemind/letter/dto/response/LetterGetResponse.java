package com.example.sharemind.letter.dto.response;

import com.example.sharemind.letter.domain.Letter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterGetResponse {

    private final Long letterId;

    private final String letterStatus;

    private final String opponentName;

    public static LetterGetResponse of(Letter letter, Boolean isCustomer) {
        String letterStatus;
        String opponentName;
        if (isCustomer) {
            letterStatus = letter.getLetterStatus().getCustomerDisplayName();
            opponentName = letter.getConsult().getCounselor().getNickname();
        } else {
            letterStatus = letter.getLetterStatus().getCounselorDisplayName();
            opponentName = letter.getConsult().getCustomer().getNickname();
        }

        return new LetterGetResponse(letter.getLetterId(), letterStatus, opponentName);
    }
}
