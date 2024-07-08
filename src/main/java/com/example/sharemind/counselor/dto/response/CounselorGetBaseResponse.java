package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.utils.CounselorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
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
        this.consultCategories = CounselorUtil.convertConsultCategories(counselor);
        this.consultTimes = CounselorUtil.convertConsultTimes(counselor);
        this.consultTypes = CounselorUtil.convertConsultTypes(counselor);
        this.consultCosts = CounselorUtil.convertConsultCosts(counselor);
        this.introduction = counselor.getIntroduction();
        this.consultStyle = counselor.getConsultStyle() == null ? null
                : counselor.getConsultStyle().getDisplayName();
    }
}
