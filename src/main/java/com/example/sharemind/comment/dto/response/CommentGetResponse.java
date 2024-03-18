package com.example.sharemind.comment.dto.response;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.global.utils.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentGetResponse {

    @Schema(description = "상담사 닉네임")
    private final String nickName;

    @Schema(description = "답변 내용")
    private final String content;

    @Schema(description = "좋아요 수")
    private final Long totalLike;

    @Schema(description = "마지막 업데이트 일시", example = "11시 10분")
    private final String updatedAt;

    @Builder
    public CommentGetResponse(String nickName, String content, Long totalLike, String updatedAt) {
        this.nickName = nickName;
        this.content = content;
        this.totalLike = totalLike;
        this.updatedAt = updatedAt;
    }

    public static CommentGetResponse of(Comment comment) {
        return CommentGetResponse.builder()
                .nickName(comment.getCounselor().getNickname())
                .content(comment.getContent())
                .totalLike(comment.getTotalLike())
                .updatedAt(TimeUtil.getUpdatedAt(comment.getUpdatedAt()))
                .build();
    }
}
