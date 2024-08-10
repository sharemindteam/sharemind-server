package com.example.sharemind.payment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentErrorCode {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역이 존재하지 않습니다."),
    PAYMENT_CUSTOMER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "구매자 결제 내역 유형이 존재하지 않습니다."),
    PAYMENT_COUNSELOR_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "판매자 수익 관리 유형이 존재하지 않습니다."),
    PAYMENT_SORT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "수익 관리 정렬 방식이 존재하지 않습니다."),
    INVALID_REFUND_WAITING(HttpStatus.BAD_REQUEST, "환불 예정으로 상태를 변경할 수 있는 결제가 아닙니다."),
    INVALID_REFUND_COMPLETE(HttpStatus.BAD_REQUEST, "환불 완료로 상태를 변경할 수 있는 결제가 아닙니다."),
    INVALID_SETTLEMENT_ONGOING(HttpStatus.BAD_REQUEST, "정산 중으로 상태를 변경할 수 있는 정산이 아닙니다."),
    INVALID_SETTLEMENT_COMPLETE(HttpStatus.BAD_REQUEST, "정산 완료로 상태를 변경할 수 있는 정산이 아닙니다."),
    PAYMENT_UPDATE_DENIED(HttpStatus.FORBIDDEN, "결제 내역 수정 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    PaymentErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
