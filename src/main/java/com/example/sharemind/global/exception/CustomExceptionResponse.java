package com.example.sharemind.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomExceptionResponse {

    private final String errorName;
    private final String message;
    private final LocalDateTime timeStamp;

    public static CustomExceptionResponse of(String errorName, String message) {
        return new CustomExceptionResponse(errorName, message, LocalDateTime.now());
    }
}
