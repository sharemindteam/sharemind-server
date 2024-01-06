package com.example.sharemind.counselor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CounselorErrorCode {

    COUNSELOR_NOT_FOUND(HttpStatus.NOT_FOUND, "상담사 정보가 존재하지 않습니다."),
    COST_NOT_FOUND(HttpStatus.NOT_FOUND, "상담료 정보가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CounselorErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
