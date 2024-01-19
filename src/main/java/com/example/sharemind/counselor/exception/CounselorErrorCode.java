package com.example.sharemind.counselor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CounselorErrorCode {

    COUNSELOR_ALREADY_EDUCATED(HttpStatus.BAD_REQUEST, "이미 교육을 수료한 상담사입니다."),
    COUNSELOR_NOT_EDUCATED(HttpStatus.FORBIDDEN, "교육을 수료하지 않은 상담사입니다."),
    COUNSELOR_NOT_FOUND(HttpStatus.NOT_FOUND, "상담사 정보가 존재하지 않습니다."),
    COUNSELOR_ALREADY_IN_EVALUATION(HttpStatus.BAD_REQUEST, "이미 프로필 심사 중입니다."),
    CONSULT_STYLE_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 스타일이 존재하지 않습니다."),
    DAY_OF_WEEK_NOT_FOUND(HttpStatus.NOT_FOUND, "요일이 존재하지 않습니다."),
    CONSULT_TIME_OVERFLOW(HttpStatus.BAD_REQUEST, "한 요일에 설정 가능한 상담 가능 시간은 최대 2개입니다."),
    CONSULT_TIME_DUPLICATE(HttpStatus.CONFLICT, "설정한 상담 가능 시간이 서로 겹칩니다."),
    COST_NOT_FOUND(HttpStatus.NOT_FOUND, "상담료 정보가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CounselorErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
