package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.ConsultCost;
import com.example.sharemind.counselor.domain.ConsultTime;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetResponse {

    @Schema(description = "닉네임", example = "연애상담마스터")
    private final String nickname;

    @Schema(description = "상담료", example = "{\"편지\": 12345, \"채팅\": 10000}")
    private final Map<String, Long> consultCosts;

    @Schema(description = "상담 카테고리", example = "[\"연애갈등\", \"짝사랑\", \"남자심리\"]")
    private final List<String> consultCategories;

    @Schema(description = "상담 가능시간", example = "{\"MON\": [\"14~15\", \"15~20\"], \"WED\": [\"11~13\"]}")
    private final Map<String, List<String>> consultTimes;

    @Schema(description = "상담 방식", example = "[\"편지\", \"채팅\"]")
    private final List<String> consultTypes;

    @Schema(description = "제목", example = "자기소개 제목")
    private final String introduction;

    @Schema(description = "레벨", example = "1")
    private final Integer level;

    @Schema(description = "총 리뷰 개수", example = "132")
    private final Long totalReview;

    @Schema(description = "리뷰 평균", example = "4.5")
    private final Double ratingAverage;

    @Schema(description = "찜여부", example = "true")
    private final Boolean isWishList;

    public static CounselorGetResponse of(Counselor counselor, boolean isWishList) {
        List<String> consultCategories = counselor.getConsultCategories().stream()
                .map(ConsultCategory::getDisplayName)
                .toList();
        List<String> consultTypes = counselor.getConsultTypes().stream()
                .map(ConsultType::getDisplayName)
                .toList();
        Map<String, List<String>> consultTimes = new HashMap<>();
        for (ConsultTime consultTime : counselor.getConsultTimes()) {
            consultTimes.put(consultTime.getDay().name(), consultTime.getTimes().stream().toList());
        }
        Map<String, Long> consultCosts = new HashMap<>();
        for (ConsultCost consultCost : counselor.getConsultCosts()) {
            consultCosts.put(consultCost.getConsultType().getDisplayName(), consultCost.getCost());
        }
        return new CounselorGetResponse(counselor.getNickname(), consultCosts, consultCategories, consultTimes,
                consultTypes,
                counselor.getIntroduction(), counselor.getLevel(), counselor.getTotalReview(),
                counselor.getRatingAverage(), isWishList);
    }
}