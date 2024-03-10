package com.example.sharemind.post.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException {

    private final PostErrorCode errorCode;

    public PostException(PostErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public PostException(PostErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
