package com.example.sharemind.comment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentErrorCode {

    COMMENT_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "상담사 당 댓글은 한 번만 작성할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CommentErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
