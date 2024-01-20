package com.example.sharemind.review.application;

import com.example.sharemind.review.domain.Review;
import com.example.sharemind.review.dto.request.ReviewSaveRequest;
import com.example.sharemind.review.exception.ReviewErrorCode;
import com.example.sharemind.review.exception.ReviewException;
import com.example.sharemind.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    @Override
    public void saveReview(ReviewSaveRequest reviewSaveRequest, Long customerId) {
        Review review = reviewRepository.findByReviewIdAndIsActivatedIsTrue(reviewSaveRequest.getReviewId())
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND,
                        reviewSaveRequest.getReviewId().toString()));
        if (!review.getConsult().getCustomer().getCustomerId().equals(customerId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_MODIFY_DENIED);
        }

        review.updateReview(reviewSaveRequest.getRating(), reviewSaveRequest.getComment());
    }
}
