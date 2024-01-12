package com.example.sharemind.counselor.content;

import com.example.sharemind.global.content.ConsultType;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConsultCost {

    private ConsultType consultType;

    @Min(value = 5000, message = "상담료는 최소 5000원이어야 합니다.")
    @Max(value = 100000, message = "상담료는 최대 100000원이어야 합니다.")
    private Long cost;

    @Builder
    public ConsultCost(ConsultType consultType, Long cost) {
        this.consultType = consultType;
        this.cost = cost;
    }
}
