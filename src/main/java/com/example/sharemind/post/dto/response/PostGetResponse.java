package com.example.sharemind.post.dto.response;

import com.example.sharemind.global.utils.TimeUtil;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetResponse {

    @Schema(description = "일대다 질문 아이디")
    private final Long postId;

    @Schema(description = "상담 카테고리", example = "연애갈등")
    private final String consultCategory;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "상담 내용")
    private final String content;

    @Schema(description = "공개/비공개 여부")
    private final Boolean isPublic;

    @Schema(description = "좋아요 수")
    private final Long totalLike;

    @Schema(description = "스크랩 수")
    private final Long totalScrap;

    @Schema(description = "마지막 업데이트 일시", example = "8분 전")
    private final String updatedAt;

    @Builder
    public PostGetResponse(Long postId, String consultCategory, String title, String content,
            Boolean isPublic, Long totalLike, Long totalScrap, String updatedAt) {
        this.postId = postId;
        this.consultCategory = consultCategory;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.totalLike = totalLike;
        this.totalScrap = totalScrap;
        this.updatedAt = updatedAt;
    }

    public static PostGetResponse of(Post post) {
        String consultCategory = post.getConsultCategory() == null ? null
                : post.getConsultCategory().getDisplayName();

        return PostGetResponse.builder()
                .postId(post.getPostId())
                .consultCategory(consultCategory)
                .title(post.getTitle())
                .content(post.getContent())
                .isPublic(post.getIsPublic())
                .totalLike(post.getTotalLike())
                .totalScrap(post.getTotalScrap())
                .updatedAt(TimeUtil.getUpdatedAt(post.getUpdatedAt()))
                .build();
    }
}
