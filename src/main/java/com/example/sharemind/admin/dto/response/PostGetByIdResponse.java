package com.example.sharemind.admin.dto.response;

import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetByIdResponse {

    @Schema(description = "공개상담 아이디")
    private final Long postId;

    @Schema(description = "상담 카테고리")
    private final String consultCategory;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "내용")
    private final String content;

    @Builder
    public PostGetByIdResponse(Long postId, String consultCategory, String title, String content) {
        this.postId = postId;
        this.consultCategory = consultCategory;
        this.title = title;
        this.content = content;
    }

    public static PostGetByIdResponse of(Post post) {
        return PostGetByIdResponse.builder()
                .postId(post.getPostId())
                .consultCategory(post.getConsultCategory().getDisplayName())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
