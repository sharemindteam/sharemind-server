package com.example.sharemind.postScrap.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.postScrap.application.PostScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PostScrap Controller", description = "일대다 상담 질문 스크랩 컨트롤러")
@RestController
@RequestMapping("/api/v1/postScraps")
@RequiredArgsConstructor
public class PostScrapController {

    private final PostScrapService postScrapService;

    @Operation(summary = "일대다 상담 스크랩 생성", description = "일대다 상담 스크랩 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400",
                    description = "이미 스크랩한 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 질문\n 2. 존재하지 않는 회원",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "일대다 질문 아이디")
    })
    @PostMapping("/{postId}")
    public ResponseEntity<Void> createPostScrap(@PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postScrapService.createPostScrap(postId, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "일대다 상담 스크랩 취소", description = "일대다 상담 스크랩 취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "취소 성공"),
            @ApiResponse(responseCode = "404", description = """
                    1. 존재하지 않는 상담
                    2. 존재하지 않는 회원
                    3. 존재하지 않는 스크랩""",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "일대다 질문 아이디")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePostScrap(@PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postScrapService.deletePostScrap(postId, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }
}
