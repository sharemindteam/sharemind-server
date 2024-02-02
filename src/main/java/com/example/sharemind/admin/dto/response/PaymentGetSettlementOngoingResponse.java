package com.example.sharemind.admin.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.payment.domain.Payment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentGetSettlementOngoingResponse {

    @Schema(description = "결제 정보 아이디")
    private final Long paymentId;

    @Schema(description = "구매자 닉네임")
    private final String customerNickname;

    @Schema(description = "상담사 닉네임")
    private final String counselorNickname;

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

    public static PaymentGetSettlementOngoingResponse of(Payment payment) {
        Consult consult = payment.getConsult();
        Counselor counselor = consult.getCounselor();

        return new PaymentGetSettlementOngoingResponse(payment.getPaymentId(), consult.getCustomer().getNickname(),
                consult.getCounselor().getNickname(), consult.getConsultType().getDisplayName(),
                consult.getConsultedAt(), consult.getCost(), counselor.getBank(), counselor.getAccount(),
                counselor.getAccountHolder());
    }
}
