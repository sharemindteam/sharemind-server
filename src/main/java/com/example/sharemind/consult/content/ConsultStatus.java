package com.example.sharemind.consult.content;

import lombok.Getter;

@Getter
public enum ConsultStatus {

    WAITING("상담 대기"),
    ONGOING("상담 중"),
    FINISH("상담 종료"),
    CANCEL("상담 취소");

    private final String displayName;

    ConsultStatus(String displayName) {
        this.displayName = displayName;
    }
}
