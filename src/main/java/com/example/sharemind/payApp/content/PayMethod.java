package com.example.sharemind.payApp.content;

import com.example.sharemind.payApp.exception.PayAppErrorCode;
import com.example.sharemind.payApp.exception.PayAppException;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PayMethod {

    CREDIT_CARD(1, "신용카드"),
    MOBILE_PHONE(2, "휴대전화"),
    OVERSEAS_PAYMENT(3, "해외결제"),
    IN_PERSON_PAYMENT(4, "대면결제"),
    BANK_TRANSFER(6, "계좌이체"),
    VIRTUAL_ACCOUNT(7, "가상계좌"),
    KAKAO_PAY(15, "카카오페이"),
    NAVER_PAY(16, "네이버페이"),
    REGISTERED_PAYMENT(17, "등록결제"),
    SMILE_PAY(21, "스마일페이"),
    APPLE_PAY(23, "애플페이");

    private final Integer number;
    private final String method;

    public static PayMethod getPayMethod(Integer number) {
        return Arrays.stream(PayMethod.values())
                .filter(method -> method.getNumber().equals(number))
                .findAny().orElseThrow(() -> new PayAppException(PayAppErrorCode.METHOD_NOT_FOUND,
                        number.toString()));
    }
}
