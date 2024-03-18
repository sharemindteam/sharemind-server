package com.example.sharemind.post.repository;

import com.example.sharemind.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    Optional<Post> findByPostIdAndIsActivatedIsTrue(Long postId);

    List<Post> findAllByIsPaidIsFalseAndIsActivatedIsTrue();

    @Query(value = "SELECT post_id FROM post WHERE post_status = 'PROCEEDING' ORDER BY RAND() LIMIT 50", nativeQuery = true)
    List<Long> findRandomProceedingPostIds();
}
