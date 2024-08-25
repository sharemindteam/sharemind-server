package com.example.sharemind.admin.dto.response;

import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetByUuidResponse {

    @Schema(description = "공개상담 아이디")
    private final UUID postUuid;

    @Schema(description = "상담 카테고리")
    private final String consultCategory;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "내용")
    private final String content;

    @Builder
    public PostGetByUuidResponse(UUID postUuid, String consultCategory, String title, String content) {
        this.postUuid = postUuid;
        this.consultCategory = consultCategory;
        this.title = title;
        this.content = content;
    }

    public static PostGetByUuidResponse of(Post post) {
        return PostGetByUuidResponse.builder()
                .postUuid(post.getPostUuid())
                .consultCategory(post.getConsultCategory().getDisplayName())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
