package com.example.sharemind.review.application;

import com.example.sharemind.review.dto.request.ReviewSaveRequest;

public interface ReviewService {
    void saveReview(ReviewSaveRequest reviewSaveRequest, Long customerId);
}
