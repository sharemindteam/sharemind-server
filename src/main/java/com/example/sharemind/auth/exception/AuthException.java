package com.example.sharemind.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public AuthException(AuthErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
