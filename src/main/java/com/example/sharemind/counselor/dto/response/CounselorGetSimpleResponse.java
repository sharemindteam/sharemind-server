package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.utils.CounselorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetSimpleResponse {

    @Schema(description = "상담사 아이디")
    private final Long counselorId;

    @Schema(description = "상담사 닉네임")
    private final String nickname;

    @Schema(description = "상담 카테고리", example = "[\"연애갈등\", \"짝사랑\", \"남자심리\"]")
    private final List<String> consultCategories;

    @Schema(description = "상담 스타일", example = "조언")
    private final String consultStyle;

    @Schema(description = "한줄 소개")
    private final String introduction;

    @Schema(description = "레벨")
    private final Integer level;

    @Schema(description = "총 리뷰 개수", example = "132")
    private final Long totalReview;

    @Schema(description = "리뷰 평균", example = "4.5")
    private final Double ratingAverage;

    public static CounselorGetSimpleResponse of(Counselor counselor) {
        return new CounselorGetSimpleResponse(counselor.getCounselorId(), counselor.getNickname(),
                CounselorUtil.convertConsultCategories(counselor), counselor.getConsultStyle().getDisplayName(),
                counselor.getIntroduction(),
                counselor.getLevel(), counselor.getTotalReview(), counselor.getRatingAverage());
    }
}
