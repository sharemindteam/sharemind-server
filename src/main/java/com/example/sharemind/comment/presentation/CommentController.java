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
            - 주소 형식: /api/v1/comments/counselors/1""")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "1. 진행중이지 않은 상담 2. 마감된 상담 중 상담사 본인이 답변을 작성하지 않은 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = """
                    - 일대다 상담 ID""")
    })
    @GetMapping("/counselors/{postId}")
    public ResponseEntity<List<CommentGetResponse>> getCounselorComments(@PathVariable Long postId,
         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "상담사 사이드 일대다 상담 댓글 작성", description = """
            - 상담사 사이드 일대다 상담 댓글 작성 API
            - 주소 형식: /api/v1/comments/counselors""")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "1. 진행중이지 않은 상담 2. 마감된 상담 중 상담사 본인이 답변을 작성하지 않은 상담 3. 이미 답변을 작성한 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping("/counselors")
    public ResponseEntity<Void> createComments(@RequestBody CommentCreateRequest commentCreateRequest,
           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        commentService.createComment(commentCreateRequest, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
