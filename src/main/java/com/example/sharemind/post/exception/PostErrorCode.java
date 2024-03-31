package com.example.sharemind.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "일대다 상담이 존재하지 않습니다."),
    POST_ALREADY_PAID(HttpStatus.BAD_REQUEST, "이미 결제 완료된 일대다 상담입니다."),
    POST_MODIFY_DENIED(HttpStatus.FORBIDDEN, "일대다 상담 질문 작성 권한이 없습니다."),
    POST_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 일대다 상담 질문이 최종 제출되었습니다."),
    POST_NOT_PROCEEDING(HttpStatus.BAD_REQUEST, "진행중인 상담이 아닙니다."),
    POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "일대다 상담 질문 접근 권한이 없습니다."),
    POST_SORT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 정렬 방식이 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
