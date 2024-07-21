package com.example.sharemind.comment.application;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.comment.dto.request.CommentCreateRequest;
import com.example.sharemind.comment.dto.response.CommentGetResponse;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    List<CommentGetResponse> getCounselorComments(UUID postUuid, Long customerId);

    List<CommentGetResponse> getCustomerComments(UUID postUuid, Long customerId);

    void createComment(CommentCreateRequest commentCreateRequest, Long customerId);

    Comment getCommentByCommentId(Long commentId);

    void updateCustomerChosenComment(UUID postUuid, Long commentId, Long customerId);

    Boolean getIsCommentOwner(UUID postUuid, Long customerId);
}
