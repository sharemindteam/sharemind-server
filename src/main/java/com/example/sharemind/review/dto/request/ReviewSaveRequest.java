package com.example.sharemind.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ReviewSaveRequest {

    @Schema(description = "리뷰 아이디")
    @NotNull(message = "리뷰 id는 공백일 수 없습니다.")
    private Long reviewId;

    @Schema(description = "평점", example = "3")
    @Min(value = 1, message = "평점은 최소 1입니다.")
    @Max(value = 5, message = "평점은 최대 5입니다.")
    @NotNull(message = "평점은 공백일 수 없습니다.")
    private Integer rating;

    @Schema(description = "리뷰 내용", example = "이 상담사는 좀... 별로인 듯?")
    @Size(max = 500, message = "리뷰 내용은 최대 500자입니다.")
    @NotBlank(message = "리뷰 내용은 공백일 수 없습니다.")
    private String comment;
}
