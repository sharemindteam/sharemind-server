package com.example.sharemind.post.dto.request;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    @Schema(description = "상담료")
    @NotNull(message = "상담료는 공백일 수 없습니다.")
    private Long cost;

    @Schema(description = "상담 공개 여부", example = "true")
    @NotNull(message = "상담 공개 여부는 공백일 수 없습니다.")
    private Boolean isPublic;

    @Schema(description = "전화번호(하이픈 필수)", example = "010-1234-5678")
    @NotBlank(message = "전화번호는 공백일 수 없습니다.")
    private String phoneNumber;

    public Post toEntity(Customer customer) {
        return Post.builder()
                .customer(customer)
                .cost(cost)
                .isPublic(isPublic)
                .customerPhoneNumber(phoneNumber)
                .build();
    }
}
