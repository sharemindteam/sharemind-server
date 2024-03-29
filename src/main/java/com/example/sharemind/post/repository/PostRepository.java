package com.example.sharemind.post.repository;

import com.example.sharemind.post.domain.Post;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    Optional<Post> findByPostIdAndIsActivatedIsTrue(Long postId);

    List<Post> findAllByIsPaidIsFalseAndIsActivatedIsTrue();

    @Query(value = "SELECT * FROM post " +
            "WHERE is_public = true AND post_status = 'COMPLETED' AND is_activated = true " +
            "AND updated_at >= :weekAgo " +
            "ORDER BY total_like DESC LIMIT :size", nativeQuery = true)
    List<Post> findPopularityPosts(LocalDate weekAgo, int size);

    @Query(value = "SELECT post_id FROM Post WHERE post_status = 'PROCEEDING' AND published_at <= CURRENT_TIMESTAMP - INTERVAL 1 DAY ORDER BY RAND() LIMIT 50", nativeQuery = true)
    List<Long> findRandomProceedingPostIdsAfter24Hours();

    @Query(value = "SELECT post_id FROM Post WHERE post_status = 'PROCEEDING' AND published_at > CURRENT_TIMESTAMP - INTERVAL 1 DAY ORDER BY RAND() LIMIT 50", nativeQuery = true)
    List<Long> findRandomProceedingPostIdsWithin24Hours();
}
