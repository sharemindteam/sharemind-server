package com.example.sharemind.payment.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.payment.domain.Payment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentGetCounselorResponse {

    @Schema(description = "payment 아이디")
    private final Long paymentId;

    @Schema(description = "구매자 닉네임")
    private final String nickname;

    @Schema(description = "채팅/편지 여부")
    private final Boolean isChat;

    @Schema(description = "순수익")
    private final Long profit;

    @Schema(description = "판매 금액")
    private final Long cost;

    @Schema(description = "수수료")
    private final Long fee;

    @Schema(description = "상담 일자", example = "2023.12.29", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private final LocalDateTime consultedAt;

    @Schema(description = "지급 일자", example = "2023.12.23", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private final LocalDateTime settledAt;

    @Schema(description = "지급 계좌")
    private final String account;

    @Builder
    public PaymentGetCounselorResponse(Long paymentId, String nickname, Boolean isChat, Long profit,
            Long cost, Long fee, LocalDateTime consultedAt, LocalDateTime settledAt,
            String account) {
        this.paymentId = paymentId;
        this.nickname = nickname;
        this.isChat = isChat;
        this.profit = profit;
        this.cost = cost;
        this.fee = fee;
        this.consultedAt = consultedAt;
        this.settledAt = settledAt;
        this.account = account;
    }


    public static PaymentGetCounselorResponse of(Payment payment) {
        Consult consult = payment.getConsult();
        Boolean isChat = consult.getChat() != null;

        return PaymentGetCounselorResponse.builder()
                .paymentId(payment.getPaymentId())
                .nickname(consult.getCustomer().getNickname())
                .isChat(isChat)
                .profit(consult.getCost() - payment.getFee())
                .cost(consult.getCost())
                .fee(payment.getFee())
                .consultedAt(consult.getConsultedAt())
                .settledAt(payment.getSettledAt())
                .account(consult.getCounselor().getAccount())
                .build();
    }
}
