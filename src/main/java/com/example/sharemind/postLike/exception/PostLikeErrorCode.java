package com.example.sharemind.postLike.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostLikeErrorCode {

    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "일대다 상담 질문 좋아요 정보가 존재하지 않습니다."),
    POST_ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 일대다 상담 질문입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
