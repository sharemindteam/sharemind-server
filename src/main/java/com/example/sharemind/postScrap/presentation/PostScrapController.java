package com.example.sharemind.postScrap.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.postScrap.application.PostScrapService;
import com.example.sharemind.postScrap.dto.response.PostScrapGetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Void> createPostScrap(@PathVariable String postId,
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
    public ResponseEntity<Void> deletePostScrap(@PathVariable String postId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postScrapService.deletePostScrap(postId, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일대다 상담 스크랩 리스트 조회", description = """
            - 특정 회원이 스크랩한 일대다 상담 리스트 조회
            - 주소 형식: /api/v1/postScraps?postScrapId=0&scrappedAt=2024-03-22T00:47:59""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postScrapId", description = """
                    - 조회 결과는 6개씩 반환하며, postScrapId로 구분
                    1. 최초 조회 요청이면 postScrapId는 0
                    2. 2번째 요청부터 postScrapId는 직전 요청의 조회 결과 6개 중 마지막 postScrapId"""),
            @Parameter(name = "scrappedAt", description = """
                    1. 최초 조회 요청이면 지금 시간
                    2. 2번째 요청부터 scrappedAt은 직전 요청의 조회 결과 6개 중 마지막 scrappedAt
                    3. 형식 예시에 적어둔 것과 꼭 맞춰주셔야 합니다""")
    })
    @GetMapping
    public ResponseEntity<List<PostScrapGetResponse>> getScrappedPosts(
            @RequestParam Long postScrapId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime scrappedAt,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(postScrapService.getScrappedPosts(postScrapId, scrappedAt,
                customUserDetails.getCustomer().getCustomerId()));
    }
}
