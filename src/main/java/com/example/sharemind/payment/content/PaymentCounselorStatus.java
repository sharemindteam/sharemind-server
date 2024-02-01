package com.example.sharemind.payment.content;

import com.example.sharemind.payment.exception.PaymentErrorCode;
import com.example.sharemind.payment.exception.PaymentException;

import java.util.Arrays;

public enum PaymentCounselorStatus {
    NONE,
    CONSULT_FINISH,
    SETTLEMENT_WAITING,
    SETTLEMENT_ONGOING,
    SETTLEMENT_COMPLETE;

    public static PaymentCounselorStatus getPaymentCounselorStatusByName(String name) {
        return Arrays.stream(PaymentCounselorStatus.values())
                .filter(counselorStatus -> counselorStatus.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(
                        () -> new PaymentException(
                                PaymentErrorCode.PAYMENT_COUNSELOR_STATUS_NOT_FOUND, name
                        )
                );
    }
}
