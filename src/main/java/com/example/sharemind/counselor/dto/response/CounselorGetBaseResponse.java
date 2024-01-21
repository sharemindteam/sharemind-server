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
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CounselorGetBaseResponse {

    @Schema(description = "상담사 닉네임")
    private String nickname;

    @Schema(description = "상담 카테고리", example = "[\"연애갈등\", \"짝사랑\", \"남자심리\"]")
    private List<String> consultCategories;

    @Schema(description = "상담 가능시간", example = "{\"MON\": [\"14~15\", \"15~20\"], \"WED\": [\"11~13\"]}")
    private Map<String, List<String>> consultTimes;

    @Schema(description = "상담 방식", example = "[\"편지\", \"채팅\"]")
    private List<String> consultTypes;

    @Schema(description = "상담료", example = "{\"편지\": 12345, \"채팅\": 10000}")
    private Map<String, Long> consultCosts;

    @Schema(description = "한줄 소개")
    private String introduction;

    @Schema(description = "상담 스타일", example = "조언")
    private String consultStyle;

    protected CounselorGetBaseResponse(Counselor counselor) {
        this.nickname = counselor.getNickname();
        this.consultCategories = convertConsultCategories(counselor);
        this.consultTimes = convertConsultTimes(counselor);
        this.consultTypes = convertConsultTypes(counselor);
        this.consultCosts = convertConsultCosts(counselor);
        this.introduction = counselor.getIntroduction();
        this.consultStyle = counselor.getConsultStyle().getDisplayName();
    }

    protected static List<String> convertConsultCategories(Counselor counselor) {
        return counselor.getConsultCategories().stream()
                .map(ConsultCategory::getDisplayName)
                .toList();
    }

    protected static List<String> convertConsultTypes(Counselor counselor) {
        return counselor.getConsultTypes().stream()
                .map(ConsultType::getDisplayName)
                .toList();
    }

    protected static Map<String, List<String>> convertConsultTimes(Counselor counselor) {
        Map<String, List<String>> consultTimes = new HashMap<>();
        for (ConsultTime consultTime : counselor.getConsultTimes()) {
            consultTimes.put(consultTime.getDay().name(), consultTime.getTimes().stream().toList());
        }
        return consultTimes;
    }

    protected static Map<String, Long> convertConsultCosts(Counselor counselor) {
        Map<String, Long> consultCosts = new HashMap<>();
        for (ConsultCost consultCost : counselor.getConsultCosts()) {
            consultCosts.put(consultCost.getConsultType().getDisplayName(), consultCost.getCost());
        }
        return consultCosts;
    }
}
