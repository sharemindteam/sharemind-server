package com.example.sharemind.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "일대다 상담이 존재하지 않습니다."),
    POST_ALREADY_PAID(HttpStatus.BAD_REQUEST, "이미 결제 완료된 일대다 상담입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
