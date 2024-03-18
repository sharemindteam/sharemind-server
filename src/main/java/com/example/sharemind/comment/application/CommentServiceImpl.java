package com.example.sharemind.comment.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.dto.response.CommentGetResponse;
import com.example.sharemind.comment.repository.CommentRepository;
import com.example.sharemind.post.application.PostService;
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
    private final CommentRepository commentRepository;

    @Override
    public List<CommentGetResponse> getCommentsByPost(Long postId) {
        Post post = postService.getPostByPostId(postId);

        List<Comment> comments = commentRepository.findByPostAndActivatedIsTrue(post);
        return comments.stream()
                .map(CommentGetResponse::of)
                .toList();
    }
}
