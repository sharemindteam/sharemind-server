package com.example.sharemind.comment.domain;

import com.example.sharemind.comment.exception.CommentErrorCode;
import com.example.sharemind.comment.exception.CommentException;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.post.domain.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "counselor_id")
    private Counselor counselor;

    @Size(max = 500, message = "상담 답변은 최대 500자입니다.")
    private String content;

    @Column(name = "is_chosen", nullable = false)
    private Boolean isChosen;

    @Column(name = "total_like", nullable = false)
    private Long totalLike;

    @Builder
    public Comment(Post post, Counselor counselor, String content) {
        this.post = post;
        this.counselor = counselor;
        this.content = content;
        isChosen = false;
        totalLike = 0L;
    }

    public void increaseTotalLike() {
        this.totalLike++;
    }

    public void decreaseTotalLike() {
        this.totalLike--;
    }

    public void checkCommentIsForPost(Post post) {
        if (!post.getPostId().equals(this.post.getPostId())) {
            throw new CommentException(CommentErrorCode.COMMENT_NOT_FOUND, commentId.toString());
        }
    }

    public void updateIsChosen() {
        this.isChosen = true;
    }
}
