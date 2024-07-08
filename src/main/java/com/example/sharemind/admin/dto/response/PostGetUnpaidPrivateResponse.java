package com.example.sharemind.admin.dto.response;

import com.example.sharemind.global.utils.EncryptionUtil;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetUnpaidPrivateResponse {

    @Schema(description = "일대다 상담 아이디")
    private final String postId;

    @Schema(description = "구매자 닉네임")
    private final String customerName;

    @Schema(description = "상담료")
    private final Long cost;

    @Schema(description = "상담 공개 여부")
    private final Boolean isPublic;

    @Schema(description = "상담 신청 일시")
    private final LocalDateTime createdAt;

    @Builder
    public PostGetUnpaidPrivateResponse(Long postId, String customerName, Long cost,
                                        Boolean isPublic, LocalDateTime createdAt) {
        this.postId = EncryptionUtil.encrypt(postId);
        this.customerName = customerName;
        this.cost = cost;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

    public static PostGetUnpaidPrivateResponse of(Post post) {
        return PostGetUnpaidPrivateResponse.builder()
                .postId(post.getPostId())
                .customerName(post.getCustomer().getNickname())
                .cost(post.getCost())
                .isPublic(post.getIsPublic())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
