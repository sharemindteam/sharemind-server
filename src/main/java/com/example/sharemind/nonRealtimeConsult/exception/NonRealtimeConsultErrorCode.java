package com.example.sharemind.nonRealtimeConsult.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NonRealtimeConsultErrorCode {

    NON_REALTIME_CONSULT_NOT_FOUND(HttpStatus.NOT_FOUND, "비실시간 상담이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    NonRealtimeConsultErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
