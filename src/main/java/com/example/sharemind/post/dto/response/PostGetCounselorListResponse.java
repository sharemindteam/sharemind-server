package com.example.sharemind.post.dto.response;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.global.utils.TimeUtil;
import com.example.sharemind.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetCounselorListResponse {

    @Schema(description = "일대다 질문 아이디")
    private final Long postId;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "상담 내용")
    private final String content;

    @Schema(description = "상담 카테고리")
    private final String consultCategory;

    @Schema(description = "공개/비공개 여부")
    private final Boolean isPublic;

    @Schema(description = "좋아요 수")
    private final Long totalLike;

    @Schema(description = "스크랩 수")
    private final Long totalScrap;

    @Schema(description = "답변 수")
    private final Long totalComment;

    @Column(name = "게시글 등록 일자")
    private final String publishedAt;

    @Column(name = "셰어 채택 여부")
    private final Boolean isChosen;

    @Builder
    public PostGetCounselorListResponse(Long postId, String title, String content, String consultCategory,
                                        Boolean isPublic, Long totalLike, Long totalScrap, Long totalComment,
                                        String publishedAt, Boolean isChosen) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.consultCategory = consultCategory;
        this.isPublic = isPublic;
        this.totalLike = totalLike;
        this.totalScrap = totalScrap;
        this.totalComment = totalComment;
        this.publishedAt = publishedAt;
        this.isChosen = isChosen;
    }

    public static PostGetCounselorListResponse of(Post post, Comment comment) {
        return PostGetCounselorListResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .consultCategory(post.getConsultCategory().getDisplayName())
                .isPublic(post.getIsPublic())
                .totalLike(post.getTotalLike())
                .totalScrap(post.getTotalScrap())
                .totalComment(post.getTotalComment())
                .publishedAt(TimeUtil.getUpdatedAt(post.getPublishedAt()))
                .isChosen(comment.getIsChosen())
                .build();
    }
}
