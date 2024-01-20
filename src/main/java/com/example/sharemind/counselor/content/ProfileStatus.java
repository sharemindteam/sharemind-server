package com.example.sharemind.counselor.content;

import lombok.Getter;

@Getter
public enum ProfileStatus {
    EVALUATION_COMPLETE("심사 통과"),
    EVALUATION_FAIL("심사 탈락"),
    EVALUATION_PENDING("심사 대기"),
    NO_PROFILE("프로필 정보 없음");

    private final String displayName;

    ProfileStatus(String displayName) {
        this.displayName = displayName;
    }
}
