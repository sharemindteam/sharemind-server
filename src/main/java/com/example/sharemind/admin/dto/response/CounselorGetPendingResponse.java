package com.example.sharemind.admin.dto.response;

import com.example.sharemind.counselor.domain.ConsultCost;
import com.example.sharemind.counselor.domain.ConsultTime;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetPendingResponse {

    @Schema(description = "상담사 아이디")
    private final Long counselorId;

    @Schema(description = "상담사 닉네임")
    private final String nickname;

    @Schema(description = "상담 카테고리", example = "[\"연애갈등\", \"짝사랑\", \"남자심리\"]")
    private final List<String> consultCategories;

    @Schema(description = "상담 스타일", example = "조언")
    private final String consultStyle;

    @Schema(description = "상담 방식", example = "[\"편지\", \"채팅\"]")
    private final List<String> consultTypes;

    @Schema(description = "상담 가능시간", example = "{\"MON\": [\"14~15\", \"15~20\"], \"WED\": [\"11~13\"]}")
    private final Map<String, List<String>> consultTimes;

    @Schema(description = "상담료", example = "{\"편지\": 12345, \"채팅\": 10000}")
    private final Map<String, Long> consultCosts;

    @Schema(description = "한줄 소개")
    private final String introduction;

    @Schema(description = "경험 소개")
    private final String experience;

    public static CounselorGetPendingResponse of(Counselor counselor) {
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

        return new CounselorGetPendingResponse(counselor.getCounselorId(), counselor.getNickname(), consultCategories,
                counselor.getConsultStyle().getDisplayName(), consultTypes, consultTimes, consultCosts,
                counselor.getIntroduction(), counselor.getExperience());
    }
}
