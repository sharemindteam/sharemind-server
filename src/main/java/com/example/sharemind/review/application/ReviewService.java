package com.example.sharemind.review.application;

import com.example.sharemind.review.dto.request.ReviewSaveRequest;
import com.example.sharemind.review.dto.response.ReviewGetResponse;
import com.example.sharemind.review.dto.response.ReviewGetShortResponse;

import java.util.List;

public interface ReviewService {
    void saveReview(ReviewSaveRequest reviewSaveRequest, Long customerId);

    List<ReviewGetResponse> getReviewsByCustomer(Boolean isCompleted, int pageNumber, Long customerId);

    List<ReviewGetResponse> getReviewsByCounselor(int pageNumber, Long customerId);

    List<ReviewGetShortResponse> getShortReviews(int pageNumber, int pageSize, Long counselorId);
}
