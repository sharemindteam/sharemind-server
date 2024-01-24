package com.example.sharemind.letter.presentation;

import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetDeadlineResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Letter Controller", description = "편지(비실시간 상담) 컨트롤러")
@RestController
@RequestMapping("/api/v1/letters")
@RequiredArgsConstructor
public class LetterController {
    private static final Boolean IS_CUSTOMER = true;
    private static final Boolean IS_COUNSELOR = false;

    private final LetterService letterService;

    @Operation(summary = "상담사 카테고리 조회", description = "구매자 첫번째 질문 시 선택하는 상담 카테고리 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 편지 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "letterId", description = "편지 아이디")
    })
    @GetMapping("/counselor-categories/{letterId}")
    public ResponseEntity<LetterGetCounselorCategoriesResponse> getCounselorCategories(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterService.getCounselorCategories(letterId));
    }

    @Operation(summary = "구매자 임시저장 카테고리 조회", description = "구매자가 첫번째 질문 임시저장 시 선택한 카테고리 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 편지 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "letterId", description = "편지 아이디")
    })
    @GetMapping("/customer-category/{letterId}")
    public ResponseEntity<String> getCustomerCategory(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterService.getCustomerCategory(letterId));
    }

    @Operation(summary = "판매자 답장 페이지 구매자 닉네임, 상담 카테고리 조회", description = "판매자 답장 시 상단에 뜨는 구매자 닉네임, 상담 카테고리 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "판매 정보 등록되지 않은 상담사",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 편지 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "letterId", description = "편지 아이디")
    })
    @GetMapping("/counselors/customer-info/{letterId}")
    public ResponseEntity<LetterGetNicknameCategoryResponse> getCustomerNicknameAndCategory(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterService.getCustomerNicknameAndCategory(letterId));
    }

    @Operation(summary = "구매자 편지 리스트 조회", description = "- 구매자 편지 리스트 조회\n " +
            "- 주소 형식: /api/v1/letters/customers?filter=true&sortType=latest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(아직 메시지가 존재하지 않는 경우 updatedAt과 recentContent는 null)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 정렬 방식으로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "filter", description = "완료/취소된 상담 제외 여부"),
            @Parameter(name = "sortType", description = "정렬 방식(LATEST, UNREAD)")
    })
    @GetMapping("/customers")
    public ResponseEntity<List<ChatLetterGetResponse>> getLettersByCustomer(@RequestParam Boolean filter, @RequestParam String sortType,
                                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(letterService.getLetters(filter, IS_CUSTOMER, sortType, customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "상담사 편지 리스트 조회", description = "- 상담사 편지 리스트 조회\n " +
            "- 주소 형식: /api/v1/letters/counselors?filter=true&sortType=latest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(아직 메시지가 존재하지 않는 경우 updatedAt과 recentContent는 null)"),
            @ApiResponse(responseCode = "403", description = "판매 정보 등록되지 않은 상담사",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 정렬 방식으로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "filter", description = "완료/취소된 상담 제외 여부"),
            @Parameter(name = "sortType", description = "정렬 방식(LATEST, UNREAD)")
    })
    @GetMapping("/counselors")
    public ResponseEntity<List<ChatLetterGetResponse>> getLetters(@RequestParam Boolean filter, @RequestParam String sortType,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(letterService.getLetters(filter, IS_COUNSELOR, sortType, customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "편지 마감기한 조회",
            description = "편지 환불까지 마감기한 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(첫번째 질문은 마감기한이 없으므로 null 반환)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 편지 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "letterId", description = "편지 아이디")
    })
    @GetMapping("/deadline/{letterId}")
    public ResponseEntity<LetterGetDeadlineResponse> getDeadline(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterService.getDeadline(letterId));
    }
}
