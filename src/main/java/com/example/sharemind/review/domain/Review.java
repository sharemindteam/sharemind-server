package com.example.sharemind.review.domain;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.review.exception.ReviewErrorCode;
import com.example.sharemind.review.exception.ReviewException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Min(value = 1, message = "평점은 최소 1입니다.")
    @Max(value = 5, message = "평점은 최대 5입니다.")
    private Integer rating;

    @Size(max = 500, message = "리뷰 내용은 최대 500자입니다.")
    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @OneToOne(mappedBy = "review", optional = false)
    private Consult consult;

    @Builder
    public Review(Consult consult) {
        this.consult = consult;
        this.isCompleted = false;
    }

    public void updateReview(Integer rating, String comment) {
        validateReview();

        this.rating = rating;
        this.comment = comment;
        this.isCompleted = true;

        this.consult.getCounselor().updateTotalReviewAndRatingAverage(this.rating);
    }

    private void validateReview() {
        if (this.isCompleted) {
            throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_COMPLETED);
        }
    }
}
