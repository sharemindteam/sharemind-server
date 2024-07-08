package com.example.sharemind.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateRequest {

    @Schema(description = "일대다 상담 아이디")
    @NotNull(message = "일대다 상담 아이디는 공백일 수 없습니다.")
    private String postId;

    @Schema(description = "선택한 상담 카테고리", example = "BOREDOM")
    @NotBlank(message = "상담 카테고리는 공백일 수 없습니다.")
    private String consultCategory;

    @Schema(description = "상담 제목", example = "남자친구의 심리가 궁금해요")
    @NotBlank(message = "상담 제목은 공백일 수 없습니다.")
    @Size(max = 50, message = "제목은 최대 50자입니다.")
    private String title;

    @Schema(description = "상담 내용", example = "안녕하세요 어쩌구저쩌구~")
    @NotBlank(message = "상담 내용은 공백일 수 없습니다.")
    @Size(max = 1000, message = "상담 내용은 최대 1000자입니다.")
    private String content;

    @Schema(description = "최종 저장 여부", example = "최종 저장이면 true, 임시 저장이면 false")
    @NotNull(message = "최종 저장 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;
}
