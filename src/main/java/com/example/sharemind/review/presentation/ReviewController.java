package com.example.sharemind.review.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.review.application.ReviewService;
import com.example.sharemind.review.dto.request.ReviewSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review Controller", description = "리뷰 컨트롤러")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 저장", description = "리뷰 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 올바르지 않은 형식의 요청 값(ex. 리뷰 아이디가 공백, 리뷰 내용이 500자 초과)\n " +
                            "2. 이미 작성된 리뷰에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403",
                    description = "작성 권한이 없는 리뷰에 대한 요청(리뷰에 해당하는 상담의 구매자가 아님)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리뷰 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PatchMapping
    public ResponseEntity<Void> saveReview(@Valid @RequestBody ReviewSaveRequest reviewSaveRequest,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reviewService.saveReview(reviewSaveRequest, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }
}
