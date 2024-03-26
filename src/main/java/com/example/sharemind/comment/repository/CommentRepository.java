package com.example.sharemind.comment.repository;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.post.domain.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomerRepository {

    List<Comment> findByPostAndIsActivatedIsTrue(Post post);

    Comment findByPostAndCounselorAndIsActivatedIsTrue(Post post, Counselor counselor);

    Optional<Comment> findByCommentIdAndIsActivatedIsTrue(Long commentId);
}
