package com.example.sharemind.commentLike.repository;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.commentLike.domain.CommentLike;
import com.example.sharemind.customer.domain.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Boolean existsByCommentAndCustomer(Comment comment, Customer customer);

    Optional<CommentLike> findByCommentAndCustomer(Comment comment, Customer customer);
}
