package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.wishList.domain.WishList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetWishListResponse extends CounselorGetBaseResponse {

    @Schema(description = "위시리스트 id", example = "1")
    private final Long wishListId;

    @Schema(description = "레벨", example = "1")
    private final Integer level;

    @Schema(description = "총 리뷰 개수", example = "132")
    private final Long totalReview;

    @Schema(description = "리뷰 평균", example = "4.5")
    private final Double ratingAverage;

    private CounselorGetWishListResponse(Counselor counselor, WishList wishList) {
        super(counselor);
        this.wishListId = wishList.getWishlistId();
        this.level = counselor.getLevel();
        this.totalReview = counselor.getTotalReview();
        this.ratingAverage = counselor.getRatingAverage();
    }

    public static CounselorGetWishListResponse of(WishList wishList) {
        return new CounselorGetWishListResponse(wishList.getCounselor(), wishList);
    }
}
