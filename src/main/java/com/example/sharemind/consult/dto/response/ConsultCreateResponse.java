package com.example.sharemind.consult.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.content.ConsultCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsultCreateResponse {

    private final Long consultId;
    private final String nickname; // TODO 이런 식의 상담사 정보를 요청하는 api가 많아진다면 나중에 dto로 묶어버려도 될 듯
    private final Integer level;
    private final Double ratingAverage;
    private final Long totalReview;
    private final Set<String> consultCategories;
    private final String consultStyle;
    private final String consultType;
    private final Long cost;

    public static ConsultCreateResponse of(Consult consult, Counselor counselor) {
        Set<String> consultCategories = counselor.getConsultCategories().stream()
                .map(ConsultCategory::getDisplayName)
                .collect(Collectors.toSet());

        return new ConsultCreateResponse(consult.getConsultId(),
                counselor.getNickname(), counselor.getLevel(), counselor.getRatingAverage(),
                counselor.getTotalReview(), consultCategories, counselor.getConsultStyle().getDisplayName(),
                consult.getConsultType().getDisplayName(), consult.getCost());
    }
}
