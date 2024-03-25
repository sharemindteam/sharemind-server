package com.example.sharemind.postLike.exception;

import lombok.Getter;

@Getter
public class PostLikeException extends RuntimeException {

    private final PostLikeErrorCode errorCode;

    public PostLikeException(PostLikeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
