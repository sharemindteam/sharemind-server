package com.example.sharemind.comment.dto.request;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentCreateRequest {

    @Schema(description = "상담 id")
    @NotNull
    private Long postId;

    @Schema(description = "content")
    private String content;

    public Comment toEntity(Post post, Counselor counselor) {
        return Comment.builder()
                .post(post)
                .counselor(counselor)
                .content(content)
                .build();
    }
}
