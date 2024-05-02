package com.example.sharemind.comment.dto.response;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.global.utils.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentGetResponse {

    @Schema(description = "댓글 id")
    private final Long commentId;

    @Schema(description = "상담사 id")
    private final Long counselorId;

    @Schema(description = "상담사 닉네임")
    private final String nickName;

    @Schema(description = "상담 스타일")
    private final String consultStyle;

    @Schema(description = "답변 내용")
    private final String content;

    @Schema(description = "조회한 회원의 좋아요 여부")
    private final Boolean isLiked;

    @Schema(description = "좋아요 수")
    private final Long totalLike;

    @Schema(description = "마지막 업데이트 일시", example = "오전 11:10")
    private final String updatedAt;

    @Schema(description = "채택 여부", example = "true")
    private final Boolean isChosen;

    @Builder
    public CommentGetResponse(Long commentId, Long counselorId, String nickName, String consultStyle, String content,
                              Boolean isLiked, Long totalLike, String updatedAt, Boolean isChosen) {
        this.commentId = commentId;
        this.counselorId = counselorId;
        this.nickName = nickName;
        this.consultStyle = consultStyle;
        this.content = content;
        this.isLiked = isLiked;
        this.totalLike = totalLike;
        this.updatedAt = updatedAt;
        this.isChosen = isChosen;
    }

    public static CommentGetResponse of(Comment comment, Boolean isLiked) {
        return CommentGetResponse.builder()
                .commentId(comment.getCommentId())
                .counselorId(comment.getCounselor().getCounselorId())
                .nickName(comment.getCounselor().getNickname())
                .consultStyle(comment.getCounselor().getConsultStyle().getDisplayName())
                .content(comment.getContent())
                .isLiked(isLiked)
                .totalLike(comment.getTotalLike())
                .updatedAt(TimeUtil.getUpdatedAt(comment.getCreatedAt()))
                .isChosen(comment.getIsChosen())
                .build();
    }
}
