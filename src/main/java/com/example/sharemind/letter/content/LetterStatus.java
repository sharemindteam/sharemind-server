package com.example.sharemind.letter.content;

import lombok.Getter;

@Getter
public enum LetterStatus {
    WAITING("상담 대기", "질문 대기"),
    FIRST_ASKING("답변 대기", "질문 도착"),
    FIRST_ANSWER("답변 도착", "질문 대기"),
    FIRST_FINISH("상담 종료", "상담 종료"),
    SECOND_ASKING("답변 대기", "질문 도착"),
    SECOND_FINISH("상담 종료", "상담 종료"),
    COUNSELOR_CANCEL("상담 취소", "상담 취소"),
    CUSTOMER_CANCEL("상담 취소", "상담 취소");

    private final String customerDisplayName;
    private final String counselorDisplayName;

    LetterStatus(String customerDisplayName, String counselorDisplayName) {
        this.customerDisplayName = customerDisplayName;
        this.counselorDisplayName = counselorDisplayName;
    }
}
