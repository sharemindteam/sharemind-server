package com.example.sharemind.post.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.dto.request.PostCreateRequest;
import com.example.sharemind.post.dto.request.PostUpdateRequest;
import com.example.sharemind.post.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Post Controller", description = "일대다 상담 질문 컨트롤러")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "일대다 상담 질문 신청", description = "일대다 상담 질문 신청")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신청 성공"),
            @ApiResponse(responseCode = "400",
                    description = "요청 값 중 공백이 존재",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Void> createPost(@Valid @RequestBody PostCreateRequest postCreateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postService.createPost(postCreateRequest, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "일대다 상담 질문 내용 작성", description = "일대다 상담 질문 내용 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작성 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 요청 값 중 공백이 존재\n 2. 이미 최종 제출된 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "작성 권한이 없는 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 일대다 상담\n 2. 존재하지 않는 상담 카테고리",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PatchMapping
    public ResponseEntity<Void> updatePost(@Valid @RequestBody PostUpdateRequest postUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postService.updatePost(postUpdateRequest, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "구매자&비로그인 사용자 일대다 상담 질문 단건 조회", description = """
            - 구매자&비로그인 사용자 일대다 상담 질문 단건 조회
            - 로그인한 사용자일 경우 헤더에 accessToken을 넣어주세요""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 상담(다른 회원의 비공개 상담 접근 시도)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일대다 질문 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postUuid", description = "일대다 질문 아이디")
    })
    @GetMapping("/{postUuid}")
    public ResponseEntity<PostGetResponse> getPost(@PathVariable UUID postUuid,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(postService.getPost(postUuid,
                customUserDetails == null ? 0 : customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "구매자 본인 일대다 상담 리스트 조회", description = """
            - 구매자 상담 탭에서 본인이 작성한 일대다 상담 질문 리스트 조회
            - 주소 형식: /api/v1/posts/customers?filter=true&postId=0""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "filter", description = "완료/취소된 상담 제외: true, 포함: false"),
            @Parameter(name = "postUuid", description = """
                    - 조회 결과는 4개씩 반환하며, postUuid로 구분
                    1. 최초 조회 요청이면 postUuid는 00000000-0000-0000-0000-000000000000
                    2. 2번째 요청부터 postUuid는 직전 요청의 조회 결과 4개 중 마지막 postUuid""")
    })
    @GetMapping("/customers")
    public ResponseEntity<List<PostGetCustomerListResponse>> getPostsByCustomer(
            @RequestParam Boolean filter,
            @RequestParam UUID postUuid,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(postService.getPostsByCustomer(filter, postUuid,
                customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "상담사 본인 일대다 상담 리스트 조회", description = """
            - 상담사 상담 탭에서 본인이 댓글 작성한 일대다 상담 질문 리스트 조회
            - 주소 형식: /api/v1/posts/counselors?filter=true&postUuid=0""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @Parameters({
            @Parameter(name = "filter", description = "완료/취소된 상담 제외: true, 포함: false"),
            @Parameter(name = "postUuid", description = """
                    - 조회 결과는 4개씩 반환하며, postUuid로 구분
                    1. 최초 조회 요청이면 postUuid는 00000000-0000-0000-0000-000000000000
                    2. 2번째 요청부터 postUuid는 직전 요청의 조회 결과 4개 중 마지막 postId""")
    })
    @GetMapping("/counselors")
    public ResponseEntity<List<PostGetCounselorListResponse>> getPostsByCounselor(
            @RequestParam Boolean filter,
            @RequestParam UUID postUuid,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(postService.getPostsByCounselor(filter, postUuid,
                customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "구매자 사이드 공개상담 탭 일대다 상담 리스트 기본순 조회", description = """
            - 구매자 사이드의 공개상담 탭에서 답변 완료된 일대다 상담 질문 리스트 기본순 조회
            - 로그인한 사용자일 경우 헤더에 accessToken을 넣어주세요
            - 주소 형식: /api/v1/posts/customers/public?postUuid=0&finishedAt=2024-03-22T00:47:59""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @Parameters({
            @Parameter(name = "postUuid", description = """
                    - 조회 결과는 4개씩 반환하며, postUuid와 finishedAt으로 구분
                    1. 최초 조회 요청이면 postUuid는 00000000-0000-0000-0000-000000000000
                    2. 2번째 요청부터 postUuid는 직전 요청의 조회 결과 4개 중 마지막 postId"""),
            @Parameter(name = "finishedAt", description = """
                    1. 최초 조회 요청이면 지금 시간
                    2. 2번째 요청부터 finishedAt은 직전 요청의 조회 결과 4개 중 마지막 finishedAt
                    3. 형식 예시에 적어둔 것과 꼭 맞춰주셔야 합니다""")
    })
    @GetMapping("/customers/public")
    public ResponseEntity<List<PostGetPublicListResponse>> getPublicPostsByCustomer(
            @RequestParam UUID postUuid,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime finishedAt,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(postService.getPublicPostsByCustomer(postUuid, finishedAt,
                customUserDetails == null ? 0 : customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "구매자 사이드 공개상담 탭 일대다 상담 리스트 인기순 조회", description = """
            - 구매자 사이드의 공개상담 탭에서 답변 완료된 일대다 상담 질문 리스트 인기순 조회
            - 로그인한 사용자일 경우 헤더에 accessToken을 넣어주세요
            - 주소 형식: /api/v1/posts/customers/likes?postId=0&finishedAt=2024-03-22T00:47:59""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @Parameters({
            @Parameter(name = "postUuid", description = """
                    - 조회 결과는 4개씩 반환하며, postUuid와 finishedAt으로 구분
                    1. 최초 조회 요청이면 postUuid는 00000000-0000-0000-0000-000000000000
                    2. 2번째 요청부터 postUuid는 직전 요청의 조회 결과 4개 중 마지막 postUuid"""),
            @Parameter(name = "finishedAt", description = """
                    1. 최초 조회 요청이면 지금 시간
                    2. 2번째 요청부터 finishedAt은 직전 요청의 조회 결과 4개 중 마지막 finishedAt
                    3. 형식 예시에 적어둔 것과 꼭 맞춰주셔야 합니다""")
    })
    @GetMapping("/customers/public/likes")
    public ResponseEntity<List<PostGetPublicListResponse>> getPopularityPosts(
            @RequestParam UUID postUuid,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime finishedAt,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(postService.getPopularityPosts(postUuid, finishedAt,
                customUserDetails == null ? 0 : customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "상담사 랜덤 일대다 상담 ID 리스트 반환", description = """
            - 상담사 일대다 상담 답변을 위한 진행중인 상담 ID 리스트를 랜덤으로 50개 반환하는 API
            - 없을 경우 빈 리스트를 반환
            - 주소 형식: /api/v1/posts/counselors/random""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/counselors/random")
    public ResponseEntity<List<UUID>> getRandomPosts() {
        return ResponseEntity.ok(postService.getRandomPosts());
    }

    @Operation(summary = "상담사 사이드 일대다 상담 질문 단건 조회", description = """
            - 상담사가 일대다 상담 질문에 대답하기 위한 상담 질문 단건 조회
            - 주소 형식: /api/v1/posts/counselors/1""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "1. 진행중이지 않은 상담 2. 마감된 상담 중 상담사 본인이 답변을 작성하지 않은 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postUuid", description = "일대다 상담 ID")
    })
    @GetMapping("/counselors/{postUuid}")
    public ResponseEntity<PostGetResponse> getPostInfo(@PathVariable UUID postUuid,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(postService.getCounselorPostContent(postUuid,
                customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "게시물 조회 시 조회한 사람이 작성자인지 여부 조회", description = """
            - 게시물 조회 시 조회한 사람이 작성자인지 여부 조회""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일대다 질문 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postUuid", description = "일대다 질문 아이디")
    })
    @GetMapping("/customers/public/{postUuid}")
    public Boolean getIsPostOwner(@PathVariable UUID postUuid,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return postService.getIsPostOwner(postUuid,
                customUserDetails == null ? 0 : customUserDetails.getCustomer().getCustomerId());
    }
}
