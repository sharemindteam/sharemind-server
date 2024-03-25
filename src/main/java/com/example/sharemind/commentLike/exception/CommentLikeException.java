package com.example.sharemind.commentLike.exception;

import lombok.Getter;

@Getter
public class CommentLikeException extends RuntimeException {

    private final CommentLikeErrorCode errorCode;

    public CommentLikeException(CommentLikeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
