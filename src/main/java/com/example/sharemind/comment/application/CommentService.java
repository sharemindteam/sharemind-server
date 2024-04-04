package com.example.sharemind.comment.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.dto.request.CommentCreateRequest;
import com.example.sharemind.comment.dto.response.CommentGetResponse;

import java.util.List;

public interface CommentService {
    List<CommentGetResponse> getCounselorComments(Long postId, Long customerId);

    List<CommentGetResponse> getCustomerComments(Long postId, Long customerId);

    void createComment(CommentCreateRequest commentCreateRequest, Long customerId);

    Comment getCommentByCommentId(Long commentId);

    void updateCustomerChosenComment(Long postId, Long commentId, Long customerId);
}
