package com.example.sharemind.post.dto.response;

import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetPopularityResponse {

    @Schema(description = "일대다 질문 아이디")
    private final Long postId;

    @Schema(description = "제목")
    private final String title;

    @Builder
    public PostGetPopularityResponse(Long postId, String title) {
        this.postId = postId;
        this.title = title;
    }

    public static PostGetPopularityResponse of(Post post) {
        return PostGetPopularityResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .build();
    }
}
