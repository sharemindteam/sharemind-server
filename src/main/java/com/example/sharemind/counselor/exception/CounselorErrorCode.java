package com.example.sharemind.counselor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CounselorErrorCode {

    COUNSELOR_ALREADY_EDUCATED(HttpStatus.BAD_REQUEST, "이미 교육을 수료한 상담사입니다."),
    COUNSELOR_NOT_EDUCATED(HttpStatus.FORBIDDEN, "교육을 수료하지 않은 상담사입니다."),
    COUNSELOR_NOT_FOUND(HttpStatus.NOT_FOUND, "상담사 정보가 존재하지 않습니다."),
    COUNSELOR_ALREADY_IN_EVALUATION(HttpStatus.BAD_REQUEST, "이미 프로필 심사 중입니다."),
    COUNSELOR_NOT_IN_EVALUATION(HttpStatus.BAD_REQUEST, "심사 중인 프로필이 아닙니다."),
    COUNSELOR_NOT_COMPLETE_EVALUATION(HttpStatus.BAD_REQUEST, "프로필 심사 완료된 상담사가 아닙니다."),
    CONSULT_STYLE_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 스타일이 존재하지 않습니다."),
    DAY_OF_WEEK_NOT_FOUND(HttpStatus.NOT_FOUND, "요일이 존재하지 않습니다."),
    CONSULT_TIME_OVERFLOW(HttpStatus.BAD_REQUEST, "한 요일에 설정 가능한 상담 가능 시간은 최대 2개입니다."),
    CONSULT_TIME_DUPLICATE(HttpStatus.CONFLICT, "설정한 상담 가능 시간이 서로 겹칩니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATE_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 전화번호입니다."),
    INVALID_CONSULT_TYPE(HttpStatus.BAD_REQUEST, "상담사가 제공하는 상담 유형이 아닙니다."),
    COST_NOT_FOUND(HttpStatus.NOT_FOUND, "상담료 정보가 존재하지 않습니다."),
    BANK_NOT_FOUND(HttpStatus.NOT_FOUND, "은행이 존재하지 않습니다."),
    COUNSELOR_SORT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 정렬 방식이 존재하지 않습니다."),
    COUNSELOR_AND_CUSTOMER_SAME(HttpStatus.BAD_REQUEST, "본인에게는 상담 신청과 댓글 작성을 할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    CounselorErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
