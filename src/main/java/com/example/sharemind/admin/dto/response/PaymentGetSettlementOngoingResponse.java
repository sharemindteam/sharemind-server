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
public class PaymentGetSettlementOngoingResponse {

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

    @Schema(description = "상담 유형", example = "편지")
    private final String consultType;

    @Schema(description = "상담 날짜", example = "2023년 1월 29일", type = "string")
    @JsonFormat(pattern = "yyyy년 MM월 dd일", timezone = "Asia/Seoul")
    private final LocalDateTime consultedAt;

    @Schema(description = "상담 금액", example = "12345")
    private final Long cost;

    @Schema(description = "은행", example = "우리은행")
    private final String bank;

    @Schema(description = "계좌번호", example = "1002961686868")
    private final String account;

    @Schema(description = "예금주명", example = "김뫄뫄")
    private final String accountHolder;

    @Builder
    public PaymentGetSettlementOngoingResponse(Long paymentId, String customerNickname,
            String customerEmail, String counselorNickname, String counselorEmail,
            String consultType, LocalDateTime consultedAt, Long cost, String bank, String account,
            String accountHolder) {
        this.paymentId = paymentId;
        this.customerNickname = customerNickname;
        this.customerEmail = customerEmail;
        this.counselorNickname = counselorNickname;
        this.counselorEmail = counselorEmail;
        this.consultType = consultType;
        this.consultedAt = consultedAt;
        this.cost = cost;
        this.bank = bank;
        this.account = account;
        this.accountHolder = accountHolder;
    }

    public static PaymentGetSettlementOngoingResponse of(Payment payment) {
        Consult consult = payment.getConsult();
        Customer customer = consult.getCustomer();
        Counselor counselor = consult.getCounselor();

        return PaymentGetSettlementOngoingResponse.builder()
                .paymentId(payment.getPaymentId())
                .customerNickname(customer.getNickname())
                .customerEmail(customer.getEmail())
                .counselorNickname(counselor.getNickname())
                .counselorEmail(counselor.getEmail())
                .consultType(consult.getConsultType().getDisplayName())
                .consultedAt(consult.getConsultedAt())
                .cost(consult.getCost())
                .bank(counselor.getBank())
                .account(counselor.getAccount())
                .accountHolder(counselor.getAccountHolder())
                .build();
    }
}
