package com.example.sharemind.admin.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConsultGetUnpaidResponse {

    @Schema(description = "상담 아이디")
    private final Long consultId;

    @Schema(description = "구매자 닉네임")
    private final String customerName;

    @Schema(description = "구매자 이메일")
    private final String customerEmail;

    @Schema(description = "판매자 닉네임")
    private final String counselorName;

    @Schema(description = "판매자 이메일")
    private final String counselorEmail;

    @Schema(description = "상담 유형", example = "편지")
    private final String consultType;

    @Schema(description = "상담료")
    private final Long cost;

    @Schema(description = "상담 신청 일시")
    private final LocalDateTime createdAt;

    @Builder
    public ConsultGetUnpaidResponse(Long consultId, String customerName, String customerEmail,
            String counselorName, String counselorEmail, String consultType, Long cost,
            LocalDateTime createdAt) {
        this.consultId = consultId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.counselorName = counselorName;
        this.counselorEmail = counselorEmail;
        this.consultType = consultType;
        this.cost = cost;
        this.createdAt = createdAt;
    }


    public static ConsultGetUnpaidResponse of(Consult consult) {
        Customer customer = consult.getCustomer();
        Counselor counselor = consult.getCounselor();

        return ConsultGetUnpaidResponse.builder()
                .consultId(consult.getConsultId())
                .customerName(customer.getNickname())
                .customerEmail(customer.getEmail())
                .counselorName(counselor.getNickname())
                .counselorEmail(counselor.getEmail())
                .consultType(consult.getConsultType().getDisplayName())
                .cost(consult.getCost())
                .createdAt(consult.getCreatedAt())
                .build();
    }
}
