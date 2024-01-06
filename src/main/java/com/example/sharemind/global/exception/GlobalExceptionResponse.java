package com.example.sharemind.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalExceptionResponse {

    private final String errorName;
    private final String message;
    private final LocalDateTime timeStamp;

    public static GlobalExceptionResponse of(String errorName, String message) {
        return new GlobalExceptionResponse(errorName, message, LocalDateTime.now());
    }
}
