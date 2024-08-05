package com.example.sharemind.admin.dto.response;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetUnpaidPrivateResponse {

    @Schema(description = "일대다 상담 아이디")
    private final Long postId;

    @Schema(description = "구매자 닉네임")
    private final String customerName;

    @Schema(description = "구매자 이메일")
    private final String customerEmail;

    @Schema(description = "상담료")
    private final Long cost;

    @Schema(description = "상담 공개 여부")
    private final Boolean isPublic;

    @Schema(description = "상담 신청 일시")
    private final LocalDateTime createdAt;

    @Builder
    public PostGetUnpaidPrivateResponse(Long postId, String customerName, String customerEmail,
            Long cost, Boolean isPublic, LocalDateTime createdAt) {
        this.postId = postId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.cost = cost;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

    public static PostGetUnpaidPrivateResponse of(Post post) {
        Customer customer = post.getCustomer();

        return PostGetUnpaidPrivateResponse.builder()
                .postId(post.getPostId())
                .customerName(customer.getNickname())
                .customerEmail(customer.getEmail())
                .cost(post.getCost())
                .isPublic(post.getIsPublic())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
