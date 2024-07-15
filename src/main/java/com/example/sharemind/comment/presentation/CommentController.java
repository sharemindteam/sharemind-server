package com.example.sharemind.comment.presentation;

import com.example.sharemind.comment.application.CommentService;
import com.example.sharemind.comment.dto.request.CommentCreateRequest;
import com.example.sharemind.comment.dto.response.CommentGetResponse;
import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment Controller", description = "일대다 상담 답변 관리 컨트롤러")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "상담사 사이드 일대다 상담 질문 단건의 댓글 조회", description = """
            - 상담사가 일대다 상담 질문에 대답하기 위한 상담 질문 단건의 댓글 조회
            - 주소 형식: /api/v1/comments/counselors/dd88d0d8-fc70-4394-b64a-726f31f48f9e""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "1. 진행중이지 않은 상담\n 2. 마감된 상담 중 상담사 본인이 답변을 작성하지 않은 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "일대다 상담 ID")
    })
    @GetMapping("/counselors/{postId}")
    public ResponseEntity<List<CommentGetResponse>> getCounselorComments(@PathVariable UUID postId,
                                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(commentService.getCounselorComments(postId,
                customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "상담사 사이드 일대다 상담 댓글 작성", description = """
            - 상담사 사이드 일대다 상담 댓글 작성 API
            - 주소 형식: /api/v1/comments/counselors""")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "1. 진행중이지 않은 상담\n 2. 마감된 상담 중 상담사 본인이 답변을 작성하지 않은 상담" +
                    "3. 이미 답변을 작성한 상담 4. 자기자신에게 댓글 작성한 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping("/counselors")
    public ResponseEntity<Void> createComments(
            @RequestBody CommentCreateRequest commentCreateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        commentService.createComment(commentCreateRequest,
                customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "구매자 사이드 일대다 상담 질문 단건의 댓글 목록 조회", description = """
            - 구매자&비로그인 사용자 일대다 상담 질문 단건의 댓글 목록 조회
            - 로그인한 사용자일 경우 헤더에 accessToken을 넣어주세요""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "접근 권한이 없는 상담(다른 회원의 비공개 상담 접근 시도)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "일대다 상담 ID")
    })
    @GetMapping("/customers/{postId}")
    public ResponseEntity<List<CommentGetResponse>> getCustomerComments(@PathVariable UUID postId,
                                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(commentService.getCustomerComments(postId,
                customUserDetails == null ? 0 : customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "구매자 사이드 답변 채택", description = """
            - 구매자(게시물 작성자)일 경우 답변 채택을 하는 API""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채택 성공"),
            @ApiResponse(responseCode = "400", description = "1. 진행중인 상담이 아닐 때",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "1. 구매자가 작성하지 않은 postId로 요청했을 때",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 사용자일 때 2. 존재하지 않는 일대다 상담 ID 일 때 "
                    + "3. 존재하지 않는 답변 ID 일 때 4. 해당 게시물에 달린 답변이 아닐 때",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "일대다 상담 ID"),
            @Parameter(name = "commentId", description = "답변 ID")
    })
    @PatchMapping("/customers/{postId}")
    public ResponseEntity<Void> updateCustomerChosenComment(@PathVariable UUID postId, @RequestParam Long commentId,
                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        commentService.updateCustomerChosenComment(postId, commentId, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상담사의 댓글 작성 여부 조회", description = """
            - 상담사가 해당 게시물에 댓글을 작성하였는지 조회하는 API""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일대다 질문 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "일대다 상담 ID"),
    })
    @GetMapping("/counselors/authentication/{postId}")
    public Boolean getIsCommentOwner(@PathVariable UUID postId,
                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return commentService.getIsCommentOwner(postId, customUserDetails.getCustomer().getCustomerId());
    }
}
