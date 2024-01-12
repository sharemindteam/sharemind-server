package com.example.sharemind.admin.dto.response;

import com.example.sharemind.consult.domain.Consult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsultsGetUnpaidResponse {

    @Schema(description = "상담 아이디")
    private final Long consultId;

    @Schema(description = "구매자 닉네임")
    private final String customerName;

    @Schema(description = "판매자 닉네임")
    private final String counselorName;

    @Schema(description = "상담 유형", example = "편지")
    private final String consultType;

    @Schema(description = "상담료")
    private final Long cost;

    @Schema(description = "상담 신청 일시")
    private final LocalDateTime createdAt;

    public static ConsultsGetUnpaidResponse of(Consult consult) {
        return new ConsultsGetUnpaidResponse(consult.getConsultId(), consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                consult.getConsultType().getDisplayName(), consult.getCost(), consult.getCreatedAt());
    }
}
