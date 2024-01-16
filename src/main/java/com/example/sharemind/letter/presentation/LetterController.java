package com.example.sharemind.letter.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.dto.response.LetterGetCounselorCategoriesResponse;
import com.example.sharemind.letter.dto.response.LetterGetNicknameCategoryResponse;
import com.example.sharemind.letter.dto.response.LetterGetResponse;
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

@CrossOrigin
@Tag(name = "Letter Controller", description = "편지(비실시간 상담) 컨트롤러")
@RestController
@RequestMapping("/api/v1/letters")
@RequiredArgsConstructor
public class LetterController {

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
    @GetMapping("/categories/{letterId}")
    public ResponseEntity<LetterGetCounselorCategoriesResponse> getCounselorCategories(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterService.getCounselorCategories(letterId));
    }

    @Operation(summary = "구매자 닉네임, 상담 카테고리 조회", description = "판매자 답장 시 상단에 뜨는 구매자 닉네임, 상담 카테고리 조회")
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
    @GetMapping("/customer-info/{letterId}")
    public ResponseEntity<LetterGetNicknameCategoryResponse> getCustomerNicknameAndCategory(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterService.getCustomerNicknameAndCategory(letterId));
    }

    // TODO 임시, 나중에 보완 필요
    @GetMapping
    public ResponseEntity<List<LetterGetResponse>> getLetters(@RequestParam Boolean isCustomer,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(letterService.getLetters(customUserDetails.getCustomer(), isCustomer));
    }
}
