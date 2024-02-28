package com.example.sharemind.post.dto.request;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostCreateRequest {

    @Schema(description = "선택한 상담 카테고리", example = "BOREDOM")
    @NotBlank(message = "상담 카테고리는 공백일 수 없습니다.")
    private String consultCategory;

    @Schema(description = "상담 제목", example = "남자친구의 심리가 궁금해요")
    @NotBlank(message = "상담 제목은 공백일 수 없습니다.")
    private String title;

    @Schema(description = "상담 내용", example = "안녕하세요 어쩌구저쩌구~")
    @NotBlank(message = "상담 내용은 공백일 수 없습니다.")
    @Size(max = 1000, message = "상담 내용은 최대 1000자입니다.")
    private String content;

    @Schema(description = "상담료")
    @NotNull(message = "상담료는 공백일 수 없습니다.")
    private Long cost;

    @Schema(description = "상담 공개 여부", example = "true")
    @NotNull(message = "상담 공개 여부는 공백일 수 없습니다.")
    private Boolean isPublic;

    public Post toEntity(Customer customer, ConsultCategory consultCategory) {
        return Post.builder()
                .customer(customer)
                .consultCategory(consultCategory)
                .title(title)
                .content(content)
                .cost(cost)
                .isPublic(isPublic)
                .build();
    }
}
