package com.example.sharemind.review.dto.response;

import com.example.sharemind.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewGetShortResponse {

    @Schema(description = "리뷰 아이디")
    private final Long reviewId;

    @Schema(description = "구매자 닉네임")
    private final String nickname;

    @Schema(description = "평점", example = "3")
    private final Integer rating;

    @Schema(description = "리뷰 내용")
    private final String comment;

    @Schema(description = "작성 일시", example = "2024년 1월 23일", type = "string")
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private final LocalDateTime updatedAt;

    public static ReviewGetShortResponse of(Review review) {
        String nickname = review.getConsult().getCustomer().getNickname().charAt(0) + "**";

        return new ReviewGetShortResponse(review.getReviewId(), nickname, review.getRating(), review.getComment(),
                review.getUpdatedAt());
    }
}
