package com.example.sharemind.admin.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.global.utils.EncryptionUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsultGetUnpaidResponse {

    @Schema(description = "상담 아이디")
    private final String consultId;

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

    public static ConsultGetUnpaidResponse of(Consult consult) {
        return new ConsultGetUnpaidResponse(EncryptionUtil.encrypt(consult.getConsultId()), consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                consult.getConsultType().getDisplayName(), consult.getCost(), consult.getCreatedAt());
    }
}
