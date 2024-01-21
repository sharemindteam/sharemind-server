package com.example.sharemind.review.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReviewIdAndIsActivatedIsTrue(Long reviewId);

    @Query("SELECT r FROM Review r JOIN FETCH r.consult c WHERE c.customer = :customer AND r.isCompleted = :isCompleted AND r.isActivated = true")
    Page<Review> findAllByCustomerAndIsCompleted(Customer customer, Boolean isCompleted, Pageable pageable);
}
