package com.example.sharemind.post.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {

    WAITING("답변 대기"),
    PROCEEDING("답변 진행 중"),
    COMPLETED("답변 완료"),
    REPORTED("신고로 인한 게시 중단");

    private final String displayName;
}
