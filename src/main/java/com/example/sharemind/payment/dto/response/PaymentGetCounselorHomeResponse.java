package com.example.sharemind.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentGetCounselorHomeResponse {

    @Schema(description = "최근 30일 판매 수익")
    private final Long month;

    @Schema(description = "미정산 금액")
    private final Long total;

    public static PaymentGetCounselorHomeResponse of(Long month, Long total) {
        return new PaymentGetCounselorHomeResponse(month, total);
    }
}
