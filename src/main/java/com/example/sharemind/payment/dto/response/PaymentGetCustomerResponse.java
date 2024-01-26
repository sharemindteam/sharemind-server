package com.example.sharemind.payment.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.payment.domain.Payment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentGetCustomerResponse {

    @Schema(description = "결제 정보 아이디")
    private final Long paymentId;

    @Schema(description = "상담사 닉네임")
    private final String nickname;

    @Schema(description = "상담 진행 상태", example = "상담 중")
    private final String status;

    @Schema(description = "상담 유형", example = "편지")
    private final String consultType;

    @Schema(description = "상담 날짜", example = "2023년 1월 29일", type = "string")
    @JsonFormat(pattern = "yyyy년 MM월 dd일", timezone = "Asia/Seoul")
    private final LocalDateTime consultedAt;

    @Schema(description = "상담 금액", example = "12345")
    private final Long cost;

    @Schema(description = "구매 날짜", example = "2023년 1월 27일", type = "string")
    @JsonFormat(pattern = "yyyy년 MM월 dd일", timezone = "Asia/Seoul")
    private final LocalDateTime paidAt;

    @Schema(description = "결제 수단", example = "외부 결제")
    private final String method;

    public static PaymentGetCustomerResponse of(Payment payment) {
        Consult consult = payment.getConsult();

        return new PaymentGetCustomerResponse(payment.getPaymentId(), consult.getCounselor().getNickname(),
                consult.getConsultStatus().getDisplayName(), consult.getConsultType().getDisplayName(),
                consult.getConsultedAt(), consult.getCost(), payment.getCreatedAt(), payment.getMethod());
    }
}
