package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CounselorGetRandomListResponse extends CounselorGetListResponse {

    @Schema(description = "정렬 기준", example = "rating")
    private final String sortType;

    private CounselorGetRandomListResponse(Counselor counselor, Boolean isWishList,
            Boolean isRealtime,
            String sortType) {
        super(counselor, isWishList, isRealtime);
        this.sortType = sortType;
    }

    public static CounselorGetRandomListResponse of(Counselor counselor, Boolean isWishList,
            Boolean isRealtime,
            String sortType) {
        return new CounselorGetRandomListResponse(counselor, isWishList, isRealtime, sortType);
    }
}
