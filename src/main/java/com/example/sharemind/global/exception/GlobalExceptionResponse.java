package com.example.sharemind.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalExceptionResponse {

    private final HttpStatus httpStatus;
    private final String message;
    private final LocalDateTime timeStamp;

    public static GlobalExceptionResponse of(HttpStatus httpStatus, String message) {
        return new GlobalExceptionResponse(httpStatus, message, LocalDateTime.now());
    }
}
