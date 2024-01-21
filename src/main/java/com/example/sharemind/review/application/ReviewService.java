package com.example.sharemind.review.application;

import com.example.sharemind.review.dto.request.ReviewSaveRequest;
import com.example.sharemind.review.dto.response.ReviewGetResponse;
import com.example.sharemind.review.dto.response.ReviewGetShortResponse;

import java.util.List;

public interface ReviewService {
    void saveReview(ReviewSaveRequest reviewSaveRequest, Long customerId);

    List<ReviewGetResponse> getReviewsByCustomer(Boolean isCompleted, Long cursorId, Long customerId);

    List<ReviewGetResponse> getReviewsByCounselor(Long cursorId, Long customerId);

    List<ReviewGetShortResponse> getShortReviewsForCounselorHome(Long customerId);

    List<ReviewGetShortResponse> getShortReviewsForCounselorProfile(Long cursorId, Long counselorId);
}
