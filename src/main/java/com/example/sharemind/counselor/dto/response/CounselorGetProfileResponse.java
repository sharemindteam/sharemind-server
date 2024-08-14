package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetProfileResponse extends CounselorGetBaseResponse {

    @Schema(description = "상담사 아이디")
    private final Long counselorId;

    @Schema(description = "경험 소개")
    private final String experience;

    @Schema(description = "전화번호")
    private final String phoneNumber;

    private CounselorGetProfileResponse(Counselor counselor) {
        super(counselor);
        this.counselorId = counselor.getCounselorId();
        this.experience = counselor.getExperience();
        this.phoneNumber = counselor.getPhoneNumber();
    }

    public static CounselorGetProfileResponse of(Counselor counselor) {
        return new CounselorGetProfileResponse(counselor);
    }
}
