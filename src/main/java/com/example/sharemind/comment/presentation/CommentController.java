package com.example.sharemind.comment.presentation;

import com.example.sharemind.comment.application.CommentService;
import com.example.sharemind.comment.dto.request.CommentCreateRequest;
import com.example.sharemind.comment.dto.response.CommentGetResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/counselors/{postId}")
    public ResponseEntity<List<CommentGetResponse>> getCounselorComments(@PathVariable Long postId,
         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, customUserDetails.getCustomer().getCustomerId()));
    }

    @PostMapping("/counselors")
    public ResponseEntity<Void> createComments(@RequestBody CommentCreateRequest commentCreateRequest,
           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        commentService.createComment(commentCreateRequest, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }
}
