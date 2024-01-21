package com.example.sharemind.review.application;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.review.domain.Review;
import com.example.sharemind.review.dto.request.ReviewSaveRequest;
import com.example.sharemind.review.dto.response.ReviewGetResponse;
import com.example.sharemind.review.dto.response.ReviewGetShortResponse;
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
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int COUNSELOR_HOME_PAGE_SIZE = 2;
    private static final int REVIEW_PAGE_SIZE = 3;
    private static final Boolean IS_CUSTOMER = true;
    private static final Boolean IS_COUNSELOR = false;

    private final CustomerService customerService;
    private final CounselorService counselorService;
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
    public List<ReviewGetResponse> getReviewsByCustomer(Boolean isCompleted, Long cursorId, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, REVIEW_PAGE_SIZE);
        Page<ReviewGetResponse> page =
                (cursorId == 0 ?
                        reviewRepository.findAllByCustomerAndIsCompleted(customer, isCompleted, pageable) :
                        reviewRepository.findAllByReviewIdLessThanAndCustomerAndIsCompleted(
                                cursorId, customer, isCompleted, pageable))
                        .map(review -> ReviewGetResponse.of(review, IS_CUSTOMER));

        return page.getContent();
    }

    @Override
    public List<ReviewGetResponse> getReviewsByCounselor(Long cursorId, Long customerId) {
        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);

        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, REVIEW_PAGE_SIZE);
        Page<ReviewGetResponse> page =
                (cursorId == 0 ?
                        reviewRepository.findAllByCounselorAndIsCompletedIsTrue(counselor, pageable) :
                        reviewRepository.findAllByReviewIdLessThanAndCounselorAndIsCompletedIsTrue(
                                cursorId, counselor, pageable))
                        .map(review -> ReviewGetResponse.of(review, IS_COUNSELOR));

        return page.getContent();
    }

    @Override
    public List<ReviewGetShortResponse> getShortReviewsForCounselorHome(Long customerId) {
        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);

        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, COUNSELOR_HOME_PAGE_SIZE);
        Page<ReviewGetShortResponse> page = reviewRepository.findAllByCounselorAndIsCompletedIsTrueOrderByUpdatedAtDesc(
                counselor, pageable)
                .map(ReviewGetShortResponse::of);

        return page.getContent();
    }

    @Override
    public List<ReviewGetShortResponse> getShortReviewsForCounselorProfile(Long cursorId, Long counselorId) {
        Counselor counselor = counselorService.getCounselorByCounselorId(counselorId);

        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, REVIEW_PAGE_SIZE);
        Page<ReviewGetShortResponse> page =
                (cursorId == 0 ?
                        reviewRepository.findAllByCounselorAndIsCompletedIsTrue(counselor, pageable) :
                        reviewRepository.findAllByReviewIdLessThanAndCounselorAndIsCompletedIsTrue(
                                cursorId, counselor, pageable))
                        .map(ReviewGetShortResponse::of);

        return page.getContent();
    }
}
