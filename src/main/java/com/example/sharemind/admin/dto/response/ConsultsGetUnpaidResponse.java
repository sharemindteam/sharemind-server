package com.example.sharemind.admin.dto.response;

import com.example.sharemind.consult.domain.Consult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsultsGetUnpaidResponse {

    private final Long consultId;
    private final String customerName;
    private final String counselorName;
    private final String consultType;
    private final Long cost;
    private final LocalDateTime createdAt;

    public static ConsultsGetUnpaidResponse of(Consult consult) {
        return new ConsultsGetUnpaidResponse(consult.getConsultId(), consult.getCustomer().getNickname(), consult.getCounselor().getNickname(),
                consult.getConsultType().getDisplayName(), consult.getCost(), consult.getCreatedAt());
    }
}
