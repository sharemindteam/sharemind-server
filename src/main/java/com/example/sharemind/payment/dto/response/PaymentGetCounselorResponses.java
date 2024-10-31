package com.example.sharemind.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentGetCounselorResponses {

    @Schema(description = "금액 합계")
    private final Long total;

    @Schema(description = "정산 정보 목록")
    private final List<PaymentGetCounselorResponse> paymentGetCounselorResponses;

    @Builder
    public PaymentGetCounselorResponses(Long total, List<PaymentGetCounselorResponse> paymentGetCounselorResponses) {
        this.total = total;
        this.paymentGetCounselorResponses = paymentGetCounselorResponses;
    }

    public static PaymentGetCounselorResponses of (Long total, List<PaymentGetCounselorResponse> paymentGetCounselorResponses) {
        return PaymentGetCounselorResponses.builder()
                .total(total)
                .paymentGetCounselorResponses(paymentGetCounselorResponses)
                .build();
    }
}
