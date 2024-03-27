package com.example.sharemind.postScrap.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostScrapErrorCode {

    POST_SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "일대다 상담 스크랩 정보가 존재하지 않습니다."),
    POST_ALREADY_SCRAPPED(HttpStatus.BAD_REQUEST, "이미 스크랩한 일대다 상담입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
