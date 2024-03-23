package com.example.sharemind.post.dto.response;

import com.example.sharemind.global.utils.TimeUtil;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetListResponse {

    @Schema(description = "일대다 질문 아이디")
    private final Long postId;

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

    @Schema(description = "답변 수")
    private final Long totalComment;

    @Schema(description = "마지막 업데이트 일시", example = "8분 전")
    private final String updatedAt;

    @Builder
    public PostGetListResponse(Long postId, String title, String content, Boolean isPublic,
            Long totalLike, Long totalScrap, Long totalComment, String updatedAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.totalLike = totalLike;
        this.totalScrap = totalScrap;
        this.totalComment = totalComment;
        this.updatedAt = updatedAt;
    }

    public static PostGetListResponse of(Post post) {
        return PostGetListResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .isPublic(post.getIsPublic())
                .totalLike(post.getTotalLike())
                .totalScrap(post.getTotalScrap())
                .totalComment(post.getTotalComment())
                .updatedAt(TimeUtil.getUpdatedAt(post.getUpdatedAt()))
                .build();
    }

    public static PostGetListResponse ofIsNotCompleted(Post post) {
        return PostGetListResponse.builder()
                .postId(post.getPostId())
                .title(null)
                .content(null)
                .isPublic(post.getIsPublic())
                .totalLike(post.getTotalLike())
                .totalScrap(post.getTotalScrap())
                .totalComment(post.getTotalComment())
                .updatedAt(TimeUtil.getUpdatedAt(post.getUpdatedAt()))
                .build();
    }
}
