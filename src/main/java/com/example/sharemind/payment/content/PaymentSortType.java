package com.example.sharemind.payment.content;

import com.example.sharemind.payment.exception.PaymentErrorCode;
import com.example.sharemind.payment.exception.PaymentException;

import java.util.Arrays;

public enum PaymentSortType {
    ALL,
    WEEK,
    MONTH;

    public static PaymentSortType getPaymentSortTypeByName(String name) {
        return Arrays.stream(PaymentSortType.values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(name))
                .findAny().orElseThrow(
                        () -> new PaymentException(
                                PaymentErrorCode.PAYMENT_SORT_TYPE_NOT_FOUND, name
                        )
                );
    }
}
