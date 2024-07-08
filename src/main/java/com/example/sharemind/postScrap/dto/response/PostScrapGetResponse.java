package com.example.sharemind.postScrap.dto.response;

import com.example.sharemind.global.utils.EncryptionUtil;
import com.example.sharemind.global.utils.TimeUtil;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.postScrap.domain.PostScrap;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostScrapGetResponse {

    @Schema(description = "스크랩 아이디")
    private final Long postScrapId;

    @Schema(description = "일대다 질문 아이디")
    private final String postId;

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

    @Schema(description = "답변 수")
    private final Long totalComment;

    @Schema(description = "일대다 상담 마지막 업데이트 일시", example = "오전 11:10")
    private final String updatedAt;

    @Schema(description = "스크랩 일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime scrappedAt;

    @Builder
    public PostScrapGetResponse(Long postScrapId, Long postId, String title, String content,
            Boolean isPublic, Boolean isLiked, Long totalLike, Boolean isScrapped, Long totalScrap,
            Long totalComment, String updatedAt, LocalDateTime scrappedAt) {
        this.postScrapId = postScrapId;
        this.postId = EncryptionUtil.encrypt(postId);
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.isLiked = isLiked;
        this.totalLike = totalLike;
        this.isScrapped = isScrapped;
        this.totalScrap = totalScrap;
        this.totalComment = totalComment;
        this.updatedAt = updatedAt;
        this.scrappedAt = scrappedAt;
    }

    public static PostScrapGetResponse of(PostScrap postScrap, Boolean isLiked) {
        Post post = postScrap.getPost();

        return PostScrapGetResponse.builder()
                .postScrapId(postScrap.getPostScrapId())
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .isPublic(post.getIsPublic())
                .isLiked(isLiked)
                .totalLike(post.getTotalLike())
                .isScrapped(true)
                .totalScrap(post.getTotalScrap())
                .totalComment(post.getTotalComment())
                .updatedAt(TimeUtil.getUpdatedAt(post.getUpdatedAt()))
                .scrappedAt(postScrap.getUpdatedAt())
                .build();
    }
}
