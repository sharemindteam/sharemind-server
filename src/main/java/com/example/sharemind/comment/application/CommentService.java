package com.example.sharemind.comment.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.dto.request.CommentCreateRequest;
import com.example.sharemind.comment.dto.response.CommentGetResponse;

import java.util.List;

public interface CommentService {
    List<CommentGetResponse> getCounselorComments(String postId, Long customerId);

    List<CommentGetResponse> getCustomerComments(String postId, Long customerId);

    void createComment(CommentCreateRequest commentCreateRequest, Long customerId);

    Comment getCommentByCommentId(Long commentId);

    void updateCustomerChosenComment(String postId, Long commentId, Long customerId);

    Boolean getIsCommentOwner(String postId, Long customerId);
}
