package com.example.sharemind.comment.presentation;

import com.example.sharemind.comment.application.CommentService;
import com.example.sharemind.comment.dto.response.CommentGetResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Comment Controller", description = "일대다 상담 답변 관리 컨트롤러")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/counselors/{postId}")
    public ResponseEntity<List<CommentGetResponse>> getCounselorComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }
}
