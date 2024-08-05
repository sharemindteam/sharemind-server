package com.example.sharemind.admin.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.payment.domain.Payment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentGetRefundWaitingResponse {

    @Schema(description = "결제 정보 아이디")
    private final Long paymentId;

    @Schema(description = "구매자 닉네임")
    private final String customerNickname;

    @Schema(description = "구매자 이메일")
    private final String customerEmail;

    @Schema(description = "상담사 닉네임")
    private final String counselorNickname;

    @Schema(description = "상담사 이메일")
    private final String counselorEmail;

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

    @Builder
    public PaymentGetRefundWaitingResponse(Long paymentId, String customerNickname,
            String customerEmail, String counselorNickname, String counselorEmail, String status,
            String consultType, LocalDateTime consultedAt, Long cost, LocalDateTime paidAt,
            String method) {
        this.paymentId = paymentId;
        this.customerNickname = customerNickname;
        this.customerEmail = customerEmail;
        this.counselorNickname = counselorNickname;
        this.counselorEmail = counselorEmail;
        this.status = status;
        this.consultType = consultType;
        this.consultedAt = consultedAt;
        this.cost = cost;
        this.paidAt = paidAt;
        this.method = method;
    }

    public static PaymentGetRefundWaitingResponse of(Payment payment) {
        Consult consult = payment.getConsult();
        Customer customer = consult.getCustomer();
        Counselor counselor = consult.getCounselor();

        return PaymentGetRefundWaitingResponse.builder()
                .paymentId(payment.getPaymentId())
                .customerNickname(customer.getNickname())
                .customerEmail(customer.getEmail())
                .counselorNickname(counselor.getNickname())
                .counselorEmail(counselor.getEmail())
                .status(consult.getConsultStatus().getDisplayName())
                .consultType(consult.getConsultType().getDisplayName())
                .consultedAt(consult.getConsultedAt())
                .cost(consult.getCost())
                .paidAt(payment.getCreatedAt())
                .method(payment.getMethod())
                .build();
    }
}
