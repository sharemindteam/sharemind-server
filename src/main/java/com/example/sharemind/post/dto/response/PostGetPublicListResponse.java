package com.example.sharemind.post.dto.response;

import com.example.sharemind.global.utils.TimeUtil;
import com.example.sharemind.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetPublicListResponse {

    @Schema(description = "일대다 질문 아이디")
    private final Long postId;

    @Schema(description = "제목")
    private final String title;

    @Schema(description = "상담 내용")
    private final String content;

    @Schema(description = "조회한 회원의 좋아요 여부")
    private final Boolean isLiked;

    @Schema(description = "좋아요 수")
    private final Long totalLike;

    @Schema(description = "조회한 회원의 스크랩 여부")
    private final Boolean isScrapped;

    @Schema(description = "스크랩 수")
    private final Long totalScrap;

    @Schema(description = "답변 수")
    private final Long totalComment;

    @Schema(description = "마지막 업데이트 일시", example = "오전 11:10")
    private final String updatedAt;

    @Schema(description = "답변 완료 일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime finishedAt;

    @Builder
    public PostGetPublicListResponse(Long postId, String title, String content, Boolean isLiked,
            Long totalLike, Boolean isScrapped, Long totalScrap, Long totalComment,
            String updatedAt, LocalDateTime finishedAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.isLiked = isLiked;
        this.totalLike = totalLike;
        this.isScrapped = isScrapped;
        this.totalScrap = totalScrap;
        this.totalComment = totalComment;
        this.updatedAt = updatedAt;
        this.finishedAt = finishedAt;
    }

    public static PostGetPublicListResponse of(Post post, Boolean isLiked, Boolean isScrapped) {
        return PostGetPublicListResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .isLiked(isLiked)
                .totalLike(post.getTotalLike())
                .isScrapped(isScrapped)
                .totalScrap(post.getTotalScrap())
                .totalComment(post.getTotalComment())
                .updatedAt(TimeUtil.getUpdatedAt(post.getPublishedAt()))
                .finishedAt(post.getFinishedAt())
                .build();
    }
}
