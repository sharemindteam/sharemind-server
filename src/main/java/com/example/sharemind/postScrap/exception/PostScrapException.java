package com.example.sharemind.postScrap.exception;

import lombok.Getter;

@Getter
public class PostScrapException extends RuntimeException {

    private final PostScrapErrorCode errorCode;

    public PostScrapException(PostScrapErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
