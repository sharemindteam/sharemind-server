package com.example.sharemind.review.repository;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReviewIdAndIsActivatedIsTrue(Long reviewId);

    @Query("SELECT r FROM Review r JOIN FETCH r.consult c " +
            "WHERE c.customer = :customer AND r.isCompleted = :isCompleted AND r.isActivated = true " +
            "ORDER BY r.reviewId DESC")
    Page<Review> findAllByCustomerAndIsCompleted(Customer customer, Boolean isCompleted, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN FETCH r.consult c " +
            "WHERE r.reviewId < :reviewId AND c.customer = :customer AND r.isCompleted = :isCompleted AND r.isActivated = true " +
            "ORDER BY r.reviewId DESC")
    Page<Review> findAllByReviewIdLessThanAndCustomerAndIsCompleted(Long reviewId, Customer customer,
                                                                    Boolean isCompleted, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN FETCH r.consult c " +
            "WHERE c.counselor = :counselor AND r.isCompleted = true AND r.isActivated = true " +
            "ORDER BY r.reviewId DESC")
    Page<Review> findAllByCounselorAndIsCompletedIsTrue(Counselor counselor, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN FETCH r.consult c " +
            "WHERE r.reviewId < :reviewId AND c.counselor = :counselor AND r.isCompleted = true AND r.isActivated = true " +
            "ORDER BY r.reviewId DESC")
    Page<Review> findAllByReviewIdLessThanAndCounselorAndIsCompletedIsTrue(Long reviewId, Counselor counselor,
                                                                           Pageable pageable);

    @Query("SELECT r FROM Review r JOIN FETCH r.consult c " +
            "WHERE c.counselor = :counselor AND r.isCompleted = true AND r.isActivated = true " +
            "ORDER BY r.updatedAt DESC LIMIT 2")
    List<Review> findTop2ByCounselorAndIsCompletedIsTrueOrderByUpdatedAtDesc(Counselor counselor);
}
