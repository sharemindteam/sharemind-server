package com.example.sharemind.commentLike.domain;

import com.example.sharemind.comment.domain.Comment;
import com.example.sharemind.commentLike.exception.CommentLikeErrorCode;
import com.example.sharemind.commentLike.exception.CommentLikeException;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long commentLikeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public CommentLike(Customer customer, Comment comment) {
        this.customer = customer;
        this.comment = comment;

        this.comment.increaseTotalLike();
    }

    public void updateIsActivatedTrue() {
        checkIsActivatedFalse();

        super.updateIsActivatedTrue();
        this.comment.increaseTotalLike();
    }

    public void updateIsActivatedFalse() {
        super.updateIsActivatedFalse();

        this.comment.decreaseTotalLike();
    }

    private void checkIsActivatedFalse() {
        if (this.isActivated()) {
            throw new CommentLikeException(CommentLikeErrorCode.COMMENT_ALREADY_LIKED);
        }
    }
}
