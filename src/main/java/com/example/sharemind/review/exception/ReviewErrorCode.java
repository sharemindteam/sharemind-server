package com.example.sharemind.review.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReviewErrorCode {

    REVIEW_MODIFY_DENIED(HttpStatus.FORBIDDEN, "리뷰 작성 권한이 없습니다."),
    REVIEW_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 작성된 리뷰입니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ReviewErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
