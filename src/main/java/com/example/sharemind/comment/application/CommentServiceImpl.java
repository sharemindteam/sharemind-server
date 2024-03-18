package com.example.sharemind.comment.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.dto.request.CommentCreateRequest;
import com.example.sharemind.comment.dto.response.CommentGetResponse;
import com.example.sharemind.comment.repository.CommentRepository;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
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

    private final PostService postService;
    private final CounselorService counselorService;
    private final CommentRepository commentRepository;

    private static final Integer MAX_COMMENTS = 5;

    @Override
    public List<CommentGetResponse> getCommentsByPost(Long postId) {
        Post post = postService.getProceedingPost(postId);

        List<Comment> comments = commentRepository.findByPostAndActivatedIsTrue(post);
        return comments.stream()
                .map(CommentGetResponse::of)
                .toList();
    }

    @Transactional
    @Override
    public void createComment(CommentCreateRequest commentCreateRequest, Long customerId) {
        Post post = postService.getProceedingPost(commentCreateRequest.getPostId());
        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);

        commentRepository.save(commentCreateRequest.toEntity(post, counselor));

        List<Comment> comments = commentRepository.findByPostAndActivatedIsTrue(post);
        if (comments.size() == MAX_COMMENTS)
            post.updatePostStatus(PostStatus.COMPLETED);
    }
}
