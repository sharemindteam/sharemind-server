package com.example.sharemind.review.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.review.application.ReviewService;
import com.example.sharemind.review.dto.request.ReviewSaveRequest;
import com.example.sharemind.review.dto.response.ReviewGetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "구매자 리뷰 조회", description = "- 구매자 리뷰 관리 탭에서 리뷰 작성/남긴 리뷰에 해당하는 리뷰 리스트 조회\n " +
            "- 주소 형식: /api/v1/reviews/customers?isCompleted=true&pageNumber=0")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(필요하지 않은 값은 null로 반환)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "isCompleted", description = "조회하려는 리뷰가 작성 완료된 리뷰인지, 작성 전인 리뷰인지"),
            @Parameter(name = "pageNumber", description = "조회 결과는 3개씩 반환하며, 페이지 번호로 구분" +
                    "(ex. pageNumber 0: 가장 앞 3개 반환, pageNumber 1: 그다음 3개 반환)")
    })
    @GetMapping("/customers")
    public ResponseEntity<List<ReviewGetResponse>> getReviewsByCustomer(@RequestParam Boolean isCompleted,
                                                                        @RequestParam int pageNumber,
                                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(reviewService.getReviewsByCustomer(isCompleted, pageNumber,
                customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "상담사 리뷰 조회", description = "- 상담사 받은 리뷰 탭에 해당하는 리뷰 리스트 조회\n " +
            "- 주소 형식: /api/v1/reviews/counselors?pageNumber=0")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(필요하지 않은 값은 null로 반환)"),
            @ApiResponse(responseCode = "403", description = "아직 프로필 심사가 완료되지 않은 상담사",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상담사",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "pageNumber", description = "조회 결과는 3개씩 반환하며, 페이지 번호로 구분" +
                    "(ex. pageNumber 0: 가장 앞 3개 반환, pageNumber 1: 그다음 3개 반환)")
    })
    @GetMapping("/counselors")
    public ResponseEntity<List<ReviewGetResponse>> getReviewsByCounselor(@RequestParam int pageNumber,
                                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(reviewService.getReviewsByCounselor(pageNumber,
                customUserDetails.getCustomer().getCustomerId()));
    }
}
