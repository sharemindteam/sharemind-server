package com.example.sharemind.comment.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {

    private final CommentErrorCode errorCode;

    public CommentException(CommentErrorCode errorCode, String message) {
        super(errorCode.getMessage() + " : " + message);
        this.errorCode = errorCode;
    }
}
