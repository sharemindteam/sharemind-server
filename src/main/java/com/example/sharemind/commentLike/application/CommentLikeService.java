package com.example.sharemind.commentLike.application;

public interface CommentLikeService {

    void createCommentLike(Long commentId, Long customerId);

    void deleteCommentLike(Long commentId, Long customerId);
}
