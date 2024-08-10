package com.example.sharemind.post.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {

    WAITING("답변 대기"),
    PROCEEDING("답변 진행 중"),
    TIME_OUT("시간 경과로 인한 답변 완료"),
    COMPLETED("답변 완료"),
    CANCELLED("상담 취소"),
    REPORTED("신고로 인한 게시 중단");

    private final String displayName;
}
