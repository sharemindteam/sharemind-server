package com.example.sharemind.review.repository;

import com.example.sharemind.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReviewIdAndIsActivatedIsTrue(Long reviewId);
}
