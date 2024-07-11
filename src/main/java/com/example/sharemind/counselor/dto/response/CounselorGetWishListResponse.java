package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.wishList.domain.WishList;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetWishListResponse extends CounselorGetBaseResponse {

    @Schema(description = "위시리스트 id", example = "1")
    private final Long wishlistId;

    @Schema(description = "상담사 아이디", example = "1")
    private final Long counselorId;

    @Schema(description = "레벨", example = "1")
    private final Integer level;

    @Schema(description = "총 리뷰 개수", example = "132")
    private final Long totalReview;

    @Schema(description = "리뷰 평균", example = "4.5")
    private final Double ratingAverage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "해당 위시리스트 업데이트 시간", example = "a")
    private final LocalDateTime updatedAt;

    @Schema(description = "상담 완료 횟수")
    private final Long totalConsult;

    @Schema(description = "현재 접속 여부", example = "true")
    private final Boolean isRealtime;

    private CounselorGetWishListResponse(Counselor counselor, WishList wishList, Boolean isRealtime) {
        super(counselor);
        this.wishlistId = wishList.getWishlistId();
        this.counselorId = counselor.getCounselorId();
        this.level = counselor.getLevel();
        this.totalReview = counselor.getTotalReview();
        this.ratingAverage = counselor.getRatingAverage();
        this.updatedAt = wishList.getUpdatedAt();
        this.totalConsult = counselor.getTotalConsult();
        this.isRealtime = isRealtime;
    }

    public static CounselorGetWishListResponse of(WishList wishList, Boolean isRealtime) {
        return new CounselorGetWishListResponse(wishList.getCounselor(), wishList, isRealtime);
    }
}
