package com.example.sharemind.review.dto.response;

import com.example.sharemind.global.utils.*;
import com.example.sharemind.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    @Schema(description = "작성 일시", example = "2023년 1월 23일")
    private final String updatedAt;

    public static ReviewGetShortResponse of(Review review) {
        return new ReviewGetShortResponse(review.getReviewId(), review.getConsult().getCustomer().getNickname(),
                review.getRating(), review.getComment(), TimeUtil.getUpdatedAtForReview(review.getUpdatedAt()));
    }
}
