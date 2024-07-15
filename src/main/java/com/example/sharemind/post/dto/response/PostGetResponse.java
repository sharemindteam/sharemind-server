package com.example.sharemind.post.dto.response;

import com.example.sharemind.global.utils.TimeUtil;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetResponse {

    @Schema(description = "일대다 질문 아이디")
    private final UUID postUuid;

    @Schema(description = "상담 카테고리", example = "권태기")
    private final String consultCategory;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "상담 내용")
    private final String content;

    @Schema(description = "공개/비공개 여부")
    private final Boolean isPublic;

    @Schema(description = "조회한 회원의 좋아요 여부")
    private final Boolean isLiked;

    @Schema(description = "좋아요 수")
    private final Long totalLike;

    @Schema(description = "조회한 회원의 스크랩 여부")
    private final Boolean isScrapped;

    @Schema(description = "스크랩 수")
    private final Long totalScrap;

    @Schema(description = "마지막 업데이트 일시", example = "오전 11:10")
    private final String updatedAt;

    @Builder
    public PostGetResponse(UUID postUuid, String consultCategory, String title, String content,
            Boolean isPublic, Boolean isLiked, Long totalLike, Boolean isScrapped, Long totalScrap,
            String updatedAt) {
        this.postUuid = postUuid;
        this.consultCategory = consultCategory;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.isLiked = isLiked;
        this.totalLike = totalLike;
        this.isScrapped = isScrapped;
        this.totalScrap = totalScrap;
        this.updatedAt = updatedAt;
    }

    public static PostGetResponse of(Post post, Boolean isLiked, Boolean isScrapped) {
        return PostGetResponse.builder()
                .postUuid(post.getPostUuid())
                .consultCategory(post.getConsultCategory().getDisplayName())
                .title(post.getTitle())
                .content(post.getContent())
                .isPublic(post.getIsPublic())
                .isLiked(isLiked)
                .totalLike(post.getTotalLike())
                .isScrapped(isScrapped)
                .totalScrap(post.getTotalScrap())
                .updatedAt(TimeUtil.getUpdatedAt(post.getPublishedAt() == null ? post.getUpdatedAt()
                        : post.getPublishedAt()))
                .build();
    }
}
