package com.example.sharemind.comment.application;

import com.example.sharemind.comment.dto.response.CommentGetResponse;

import java.util.List;

public interface CommentService {
    List<CommentGetResponse> getCommentsByPost(Long postId);
}
