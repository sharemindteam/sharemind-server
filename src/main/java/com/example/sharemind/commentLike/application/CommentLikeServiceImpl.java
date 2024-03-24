package com.example.sharemind.commentLike.application;

import com.example.sharemind.comment.application.CommentService;
import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.commentLike.domain.CommentLike;
import com.example.sharemind.commentLike.exception.CommentLikeErrorCode;
import com.example.sharemind.commentLike.exception.CommentLikeException;
import com.example.sharemind.commentLike.repository.CommentLikeRepository;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CustomerService customerService;
    private final CommentService commentService;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    @Override
    public void createCommentLike(Long commentId, Long customerId) {
        Comment comment = commentService.getCommentByCommentId(commentId);
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        if (commentLikeRepository.existsByCommentAndCustomer(comment, customer)) {
            CommentLike commentLike = commentLikeRepository.findByCommentAndCustomer(comment,
                    customer).orElseThrow(
                    () -> new CommentLikeException(CommentLikeErrorCode.COMMENT_LIKE_NOT_FOUND));

            commentLike.updateIsActivatedTrue();
        } else {
            CommentLike commentLike = CommentLike.builder()
                    .comment(comment)
                    .customer(customer)
                    .build();

            commentLikeRepository.save(commentLike);
        }
    }
}
