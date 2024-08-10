package com.example.sharemind.payment.content;

import com.example.sharemind.payment.exception.PaymentErrorCode;
import com.example.sharemind.payment.exception.PaymentException;

import java.util.Arrays;

public enum PaymentCustomerStatus {
    NONE,
    PAYMENT_COMPLETE,
    REFUND_WAITING,
    REFUND_COMPLETE;

    public static PaymentCustomerStatus getPaymentCustomerStatusByName(String name) {
        return Arrays.stream(PaymentCustomerStatus.values())
                .filter(customerStatus -> customerStatus.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(
                        () -> new PaymentException(
                                PaymentErrorCode.PAYMENT_CUSTOMER_STATUS_NOT_FOUND, name
                        )
                );
    }
}
