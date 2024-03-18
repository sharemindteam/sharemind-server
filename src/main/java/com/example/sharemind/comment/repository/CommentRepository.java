package com.example.sharemind.comment.repository;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostAndIsActivatedIsTrue(Post post);
}
