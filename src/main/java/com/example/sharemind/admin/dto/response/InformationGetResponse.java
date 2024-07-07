package com.example.sharemind.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InformationGetResponse {

    @Schema(description = "전제 회원 수")
    private final Long customers;

    @Schema(description = "인증 완료된 상담사 수")
    private final Long completedCounselors;

    @Schema(description = "인증 대기 중인 상담사 수")
    private final Long waitingCounselors;

    @Schema(description = "완료된 채팅 수")
    private final Long completedChats;

    @Schema(description = "완료된 채팅 금액")
    private final Long completedChatCosts;

    @Schema(description = "취소된 채팅 수")
    private final Long canceledChats;

    @Schema(description = "취소된 채팅 금액")
    private final Long canceledChatCosts;

    @Schema(description = "완료된 편지 수")
    private final Long completedLetters;

    @Schema(description = "완료된 편지 금액")
    private final Long completedLetterCosts;

    @Schema(description = "취소된 편지 수")
    private final Long canceledLetters;

    @Schema(description = "취소된 편지 금액")
    private final Long canceledLetterCosts;

    @Schema(description = "공개상담 수")
    private final Long publicPosts;

    @Schema(description = "답변 완료된 공개상담 수")
    private final Long completedPublicPosts;

    @Schema(description = "비공개 상담 수")
    private final Long secretPosts;

    @Schema(description = "비공개 상담 금액")
    private final Long secretPostCosts;

    @Schema(description = "답변 완료된 비공개상담 수")
    private final Long completedSecretPosts;

    @Schema(description = "답변 완료된 비공개상담 금액")
    private final Long completedSecretPostCosts;

    @Builder
    public InformationGetResponse(Long customers, Long completedCounselors, Long waitingCounselors,
            Long completedChats, Long completedChatCosts, Long canceledChats,
            Long canceledChatCosts, Long completedLetters, Long completedLetterCosts,
            Long canceledLetters, Long canceledLetterCosts, Long publicPosts,
            Long completedPublicPosts, Long secretPosts, Long secretPostCosts,
            Long completedSecretPosts, Long completedSecretPostCosts) {
        this.customers = customers;
        this.completedCounselors = completedCounselors;
        this.waitingCounselors = waitingCounselors;
        this.completedChats = completedChats;
        this.completedChatCosts = completedChatCosts;
        this.canceledChats = canceledChats;
        this.canceledChatCosts = canceledChatCosts;
        this.completedLetters = completedLetters;
        this.completedLetterCosts = completedLetterCosts;
        this.canceledLetters = canceledLetters;
        this.canceledLetterCosts = canceledLetterCosts;
        this.publicPosts = publicPosts;
        this.completedPublicPosts = completedPublicPosts;
        this.secretPosts = secretPosts;
        this.secretPostCosts = secretPostCosts;
        this.completedSecretPosts = completedSecretPosts;
        this.completedSecretPostCosts = completedSecretPostCosts;
    }

    public static InformationGetResponse of(Long customers, Long completedCounselors,
            Long waitingCounselors, Long completedChats, Long completedChatCosts,
            Long canceledChats, Long canceledChatCosts, Long completedLetters,
            Long completedLetterCosts, Long canceledLetters, Long canceledLetterCosts,
            Long publicPosts, Long completedPublicPosts, Long secretPosts, Long secretPostCosts,
            Long completedSecretPosts, Long completedSecretPostCosts) {
        return InformationGetResponse.builder()
                .customers(customers)
                .completedCounselors(completedCounselors)
                .waitingCounselors(waitingCounselors)
                .completedChats(completedChats)
                .completedChatCosts(completedChatCosts)
                .canceledChats(canceledChats)
                .canceledChatCosts(canceledChatCosts)
                .completedLetters(completedLetters)
                .completedLetterCosts(completedLetterCosts)
                .canceledLetters(canceledLetters)
                .canceledLetterCosts(canceledLetterCosts)
                .publicPosts(publicPosts)
                .completedPublicPosts(completedPublicPosts)
                .secretPosts(secretPosts)
                .secretPostCosts(secretPostCosts)
                .completedSecretPosts(completedSecretPosts)
                .completedSecretPostCosts(completedSecretPostCosts)
                .build();
    }
}
