package com.example.sharemind.comment.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.dto.request.CommentCreateRequest;
import com.example.sharemind.comment.dto.response.CommentGetResponse;
import com.example.sharemind.comment.exception.CommentErrorCode;
import com.example.sharemind.comment.exception.CommentException;
import com.example.sharemind.comment.repository.CommentRepository;
import com.example.sharemind.commentLike.repository.CommentLikeRepository;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.content.PostStatus;
import com.example.sharemind.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final Integer MAX_COMMENTS = 5;
    private static final Boolean COMMENT_IS_NOT_LIKED = false;

    private final PostService postService;
    private final CounselorService counselorService;
    private final CustomerService customerService;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public List<CommentGetResponse> getCounselorComments(Long postId, Long customerId) {
        Post post = postService.checkAndGetCounselorPost(postId, customerId);
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        List<Comment> comments = commentRepository.findByPostAndIsActivatedIsTrue(post);
        return comments.stream()
                .map(comment -> CommentGetResponse.of(comment,
                        commentLikeRepository.existsByCommentAndCustomerAndIsActivatedIsTrue(
                                comment, customer)))
                .toList();
    }

    @Override
    public List<CommentGetResponse> getCustomerComments(Long postId, Long customerId) {
        Post post = postService.getPostByPostId(postId);
        post.checkReadAuthority(customerId);

        List<Comment> comments = commentRepository.findByPostAndIsActivatedIsTrue(post);

        if (customerId != 0) {
            Customer customer = customerService.getCustomerByCustomerId(customerId);

            return comments.stream()
                    .map(comment -> CommentGetResponse.of(comment,
                            commentLikeRepository.existsByCommentAndCustomerAndIsActivatedIsTrue(
                                    comment, customer)))
                    .toList();
        }

        return comments.stream()
                .map(comment -> CommentGetResponse.of(comment, COMMENT_IS_NOT_LIKED))
                .toList();
    }

    @Transactional
    @Override
    public void createComment(CommentCreateRequest commentCreateRequest, Long customerId) {
        Post post = postService.checkAndGetCounselorPost(commentCreateRequest.getPostId(),
                customerId);
        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);

        if (commentRepository.findByPostAndCounselorAndIsActivatedIsTrue(post, counselor) != null) {
            throw new CommentException(CommentErrorCode.COMMENT_ALREADY_REGISTERED,
                    counselor.getNickname());
        }

        commentRepository.save(commentCreateRequest.toEntity(post, counselor));
        post.increaseTotalComment();

        List<Comment> comments = commentRepository.findByPostAndIsActivatedIsTrue(post);
        if (comments.size() == MAX_COMMENTS) {
            post.updatePostStatus(PostStatus.COMPLETED);
        }
    }

    @Override
    public Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findByCommentIdAndIsActivatedIsTrue(commentId).orElseThrow(
                () -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND,
                        commentId.toString()));
    }
}
