package com.example.sharemind.review.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewGetResponse {

    @Schema(description = "리뷰 아이디")
    private final Long reviewId;

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "상담 스타일", example = "팩폭")
    private final String consultStyle;

    @Schema(description = "레벨", example = "1")
    private final Integer level;

    @Schema(description = "리뷰 평점")
    private final Double ratingAverage;

    @Schema(description = "총 리뷰 수")
    private final Long totalReview;

    @Schema(description = "상담 방식", example = "편지")
    private final String consultType;

    @Schema(description = "상담 날짜", example = "2024년 1월 12일", type = "string")
    @JsonFormat(pattern = "yyyy년 MM월 dd일", timezone = "Asia/Seoul")
    private final LocalDateTime consultedAt;

    @Schema(description = "상담료", example = "20000")
    private final Long consultCost;

    @Schema(description = "평점", example = "3")
    private final Integer rating;

    @Schema(description = "리뷰 내용")
    private final String comment;

    public static ReviewGetResponse of(Review review, Boolean isCustomer) {
        Consult consult = review.getConsult();

        if (isCustomer) {
            Counselor counselor = consult.getCounselor();
            return new ReviewGetResponse(review.getReviewId(), counselor.getNickname(),
                    counselor.getConsultStyle().getDisplayName(), counselor.getLevel(), counselor.getRatingAverage(),
                    counselor.getTotalReview(), consult.getConsultType().getDisplayName(), consult.getConsultedAt(),
                    consult.getCost(), review.getRating(), review.getComment());
        } else {
            String nickname = consult.getCustomer().getNickname().charAt(0) + "**";
            return new ReviewGetResponse(review.getReviewId(), nickname, null, null, null,
                    null, consult.getConsultType().getDisplayName(), consult.getConsultedAt(),
                    consult.getCost(), review.getRating(), review.getComment());
        }
    }
}
