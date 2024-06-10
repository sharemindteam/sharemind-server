package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetListResponse extends CounselorGetBaseResponse {

    @Schema(description = "상담사 아이디")
    private final Long counselorId;

    @Schema(description = "레벨", example = "1")
    private final Integer level;

    @Schema(description = "총 리뷰 개수", example = "132")
    private final Long totalReview;

    @Schema(description = "리뷰 평균", example = "4.5")
    private final Double ratingAverage;

    @Schema(description = "찜여부", example = "true")
    private final Boolean isWishList;

    @Schema(description = "현재 접속 여부", example = "true")
    private final Boolean isRealtime;

    private CounselorGetListResponse(Counselor counselor, Boolean isWishList, Boolean isRealtime) {
        super(counselor);
        this.counselorId = counselor.getCounselorId();
        this.level = counselor.getLevel();
        this.totalReview = counselor.getTotalReview();
        this.ratingAverage = counselor.getRatingAverage();
        this.isWishList = isWishList;
        this.isRealtime = isRealtime;
    }

    public static CounselorGetListResponse of(Counselor counselor, Boolean isWishList, Boolean isRealtime) {
        return new CounselorGetListResponse(counselor, isWishList, isRealtime);
    }
}
