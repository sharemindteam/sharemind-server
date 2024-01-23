package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CounselorGetForConsultResponse {

    @Schema(description = "상담사 아이디", example = "1")
    private final Long counselorId;

    @Schema(description = "상담사 닉네임", example = "이롸롸")
    private final String nickname;

    @Schema(description = "상담사 레벨", example = "1")
    private final Integer level;

    @Schema(description = "리뷰 평점", example = "3.5")
    private final Double ratingAverage;

    @Schema(description = "총 리뷰 수", example = "123")
    private final Long totalReview;

    @Schema(description = "상담 카테고리", example = "[\"연애갈등\", \"짝사랑\", \"남자심리\"]")
    private final List<String> consultCategories;

    @Schema(description = "상담 스타일", example = "팩폭")
    private final String consultStyle;

    @Schema(description = "선택한 상담 유형", example = "편지")
    private final String consultType;

    @Schema(description = "상담료", example = "10000")
    private final Long cost;

    public static CounselorGetForConsultResponse of(Counselor counselor, ConsultType consultType) {
        List<String> consultCategories = counselor.getConsultCategories().stream()
                .map(ConsultCategory::getDisplayName)
                .toList();

        return new CounselorGetForConsultResponse(counselor.getCounselorId(), counselor.getNickname(),
                counselor.getLevel(), counselor.getRatingAverage(), counselor.getTotalReview(), consultCategories,
                counselor.getConsultStyle().getDisplayName(), consultType.getDisplayName(),
                counselor.getConsultCost(consultType));
    }
}
