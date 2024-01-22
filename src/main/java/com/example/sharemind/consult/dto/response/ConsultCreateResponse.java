package com.example.sharemind.consult.dto.response;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.content.ConsultCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsultCreateResponse {

    @Schema(description = "상담 아이디", example = "1")
    private final Long consultId;

    @Schema(description = "상담사 닉네임", example = "이롸롸")
    private final String nickname; // TODO 이런 식의 상담사 정보를 요청하는 api가 많아진다면 나중에 dto로 묶어버려도 될 듯

    @Schema(description = "상담사 레벨", example = "1")
    private final Integer level;

    @Schema(description = "리뷰 평점", example = "3.5")
    private final Double ratingAverage;

    @Schema(description = "총 리뷰 수", example = "123")
    private final Long totalReview;

    @Schema(description = "상담 카테고리", example = "[\"연애갈등\", \"짝사랑\", \"남자심리\"]")
    private final Set<String> consultCategories;

    @Schema(description = "상담 스타일", example = "팩폭")
    private final String consultStyle;

    @Schema(description = "선택한 상담 종류", example = "편지")
    private final String consultType;

    @Schema(description = "상담료", example = "10000")
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
