package com.example.sharemind.post.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {

    WAITING("상담 대기"),
    PROCEEDING("상담 진행 중"),
    COMPLETED("상담 마감"),
    REPORTED("신고로 인한 게시 중단");

    private final String displayName;
}
