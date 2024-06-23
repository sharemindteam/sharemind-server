package com.example.sharemind.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode {

    EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 회원으로 등록된 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    CUSTOMER_BANNED(HttpStatus.FORBIDDEN, "로그인이 제한된 사용자입니다."),
    DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호가 현재 비밀번호와 동일합니다."),
    INVALID_QUIT_CONSULT(HttpStatus.BAD_REQUEST, "미완료 상담이 있어 탈퇴할 수 없습니다."),
    INVALID_QUIT_PAYMENT(HttpStatus.BAD_REQUEST, "처리되지 않은 환불금 또는 정산금이 있어 탈퇴할 수 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "access token이 이미 만료되었거나 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh token이 이미 만료되었거나 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
