package com.example.sharemind.admin.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CounselorGetByNicknameOrEmailResponse {

    @Schema(description = "상담사 아이디")
    private final Long counselorId;

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "이메일")
    private final String email;

    @Schema(description = "전화번호")
    private final String phoneNumber;

    @Schema(description = "프로필 상태")
    private final String profileStatus;

    @Builder
    public CounselorGetByNicknameOrEmailResponse(Long counselorId, String nickname, String email,
            String phoneNumber, String profileStatus) {
        this.counselorId = counselorId;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileStatus = profileStatus;
    }

    public static CounselorGetByNicknameOrEmailResponse of(Counselor counselor) {
        return CounselorGetByNicknameOrEmailResponse.builder()
                .counselorId(counselor.getCounselorId())
                .nickname(counselor.getNickname())
                .email(counselor.getEmail())
                .phoneNumber(counselor.getPhoneNumber())
                .profileStatus(counselor.getProfileStatus().getDisplayName())
                .build();
    }
}
