package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.utils.CounselorUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
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

    @Schema(description = "상담 횟수")
    private final Long totalConsult;

    @Schema(description = "한줄 소개")
    private final String introduction;

    @Schema(description = "경험 소개")
    private final String experience;

    @Schema(description = "찜여부")
    private final Boolean isWishList;

    @Builder
    public CounselorGetMinderProfileResponse(Long counselorId, String nickname, Integer level,
            Long totalReview, Double ratingAverage, List<String> consultCategories,
            String consultStyle, List<String> consultTypes, Map<String, List<String>> consultTimes,
            Map<String, Long> consultCosts, Long totalConsult, String introduction,
            String experience, Boolean isWishList) {
        this.counselorId = counselorId;
        this.nickname = nickname;
        this.level = level;
        this.totalReview = totalReview;
        this.ratingAverage = ratingAverage;
        this.consultCategories = consultCategories;
        this.consultStyle = consultStyle;
        this.consultTypes = consultTypes;
        this.consultTimes = consultTimes;
        this.consultCosts = consultCosts;
        this.totalConsult = totalConsult;
        this.introduction = introduction;
        this.experience = experience;
        this.isWishList = isWishList;
    }

    public static CounselorGetMinderProfileResponse of(Counselor counselor, Boolean isWishList) {
        return CounselorGetMinderProfileResponse.builder()
                .counselorId(counselor.getCounselorId())
                .nickname(counselor.getNickname())
                .level(counselor.getLevel())
                .totalReview(counselor.getTotalReview())
                .ratingAverage(counselor.getRatingAverage())
                .consultCategories(CounselorUtil.convertConsultCategories(counselor))
                .consultStyle(counselor.getConsultStyle().getDisplayName())
                .consultTypes(CounselorUtil.convertConsultTypes(counselor))
                .consultTimes(CounselorUtil.convertConsultTimes(counselor))
                .consultCosts(CounselorUtil.convertConsultCosts(counselor))
                .totalConsult(counselor.getTotalConsult())
                .introduction(counselor.getIntroduction())
                .experience(counselor.getExperience())
                .isWishList(isWishList)
                .build();
    }
}
