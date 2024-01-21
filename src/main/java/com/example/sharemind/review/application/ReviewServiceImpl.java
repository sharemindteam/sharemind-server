package com.example.sharemind.review.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.review.domain.Review;
import com.example.sharemind.review.dto.request.ReviewSaveRequest;
import com.example.sharemind.review.dto.response.ReviewGetResponse;
import com.example.sharemind.review.exception.ReviewErrorCode;
import com.example.sharemind.review.exception.ReviewException;
import com.example.sharemind.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final int REVIEW_PAGE_SIZE = 3;

    private final CustomerService customerService;
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

    @Override
    public List<ReviewGetResponse> getReviewsByCustomer(Boolean isCompleted, int pageNumber, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        Pageable pageable = PageRequest.of(pageNumber, REVIEW_PAGE_SIZE);
        Page<ReviewGetResponse> page = reviewRepository.findAllByCustomerAndIsCompleted(customer, isCompleted, pageable)
                .map(review -> ReviewGetResponse.of(review, true));

        return page.getContent();
    }
}
