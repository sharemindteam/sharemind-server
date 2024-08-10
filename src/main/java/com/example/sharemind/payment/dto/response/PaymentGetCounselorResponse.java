package com.example.sharemind.payment.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.payment.domain.Payment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.example.sharemind.global.constants.Constants.FEE;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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

    @Schema(description = "지급 일자", example = "2023.12.23", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private final LocalDateTime approvedAt;

    @Schema(description = "지급 계좌")
    private final String account;

    @Schema(description = "금액 합계")
    private final Long total;

    public static PaymentGetCounselorResponse of(Payment payment, Long total) {
        Consult consult = payment.getConsult();
        Boolean isChat = consult.getChat() != null;

        return new PaymentGetCounselorResponse(payment.getPaymentId(), consult.getCustomer().getNickname(), isChat,
                consult.getCost() - FEE, consult.getCost(), FEE, payment.getApprovedAt(),
                consult.getCounselor().getAccount(), total);
    }
}
