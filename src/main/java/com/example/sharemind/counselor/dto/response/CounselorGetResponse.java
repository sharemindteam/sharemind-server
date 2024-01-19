package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.ConsultCost;
import com.example.sharemind.counselor.domain.ConsultTime;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetResponse {

    @Schema(description = "닉네임", example = "연애상담마스터")
    private final String nickname;

    @Schema(description = "상담료", example = "채팅 5000원")
    private final Set<ConsultCost> consultCosts;

    @Schema(description = "상담 카테고리", example = "이별/재회")
    private final Set<ConsultCategory> consultCategories;

    @Schema(description = "상담 가능 시간", example = "월 13~15")
    private final Set<ConsultTime> consultTimes;

    @Schema(description = "상담 방식", example = "채팅, 편지")
    private final Set<ConsultType> consultTypes;

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
}
