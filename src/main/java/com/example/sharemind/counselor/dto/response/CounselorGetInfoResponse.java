package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.domain.Counselor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CounselorGetInfoResponse {

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "레벨")
    private final Integer level;
    
    @Schema(description = "상담 스타일")
    private final String consultStyle;

    @Schema(description = "교육 수료 여부")
    private final Boolean isEducated;

    @Schema(description = "프로필 정보 상태", example = "프로필 정보 없음")
    private final String profileStatus;

    public static CounselorGetInfoResponse of() {
        return new CounselorGetInfoResponse("연애상담마스터", 0, null, false,
                ProfileStatus.NO_PROFILE.name());
    }

    public static CounselorGetInfoResponse of(Counselor counselor) {
        String consultStyle = null;
        if (counselor.getConsultStyle() != null) {
            consultStyle = counselor.getConsultStyle().getDisplayName();
        }
        
        return new CounselorGetInfoResponse(counselor.getNickname(), counselor.getLevel(), consultStyle, counselor.getIsEducated(),
                counselor.getProfileStatus().name());
    }
}
