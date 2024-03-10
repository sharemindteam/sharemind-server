package com.example.sharemind.post.repository;

import com.example.sharemind.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByPostIdAndIsActivatedIsTrue(Long postId);

    List<Post> findAllByIsPaidIsFalseAndIsActivatedIsTrue();
}
