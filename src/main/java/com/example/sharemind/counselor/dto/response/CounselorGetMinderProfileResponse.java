package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.utils.CounselorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetMinderProfileResponse {

    @Schema(description = "상담사 아이디")
    private final Long counselorId;

    @Schema(description = "상담사 닉네임")
    private final String nickname;

    @Schema(description = "레벨")
    private final Integer level;

    @Schema(description = "총 리뷰 개수", example = "132")
    private final Long totalReview;

    @Schema(description = "리뷰 평균", example = "4.5")
    private final Double ratingAverage;

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

    @Schema(description = "경험 소개")
    private final String experience;

    @Schema(description = "찜여부")
    private final Boolean isWishList;

    public static CounselorGetMinderProfileResponse of(Counselor counselor, Boolean isWishList) {
        return new CounselorGetMinderProfileResponse(counselor.getCounselorId(), counselor.getNickname(),
                counselor.getLevel(), counselor.getTotalReview(), counselor.getRatingAverage(),
                CounselorUtil.convertConsultCategories(counselor), counselor.getConsultStyle().getDisplayName(),
                CounselorUtil.convertConsultTypes(counselor), CounselorUtil.convertConsultTimes(counselor),
                CounselorUtil.convertConsultCosts(counselor), counselor.getExperience(), isWishList);
    }
}
