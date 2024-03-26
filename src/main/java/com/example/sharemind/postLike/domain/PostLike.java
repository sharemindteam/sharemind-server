package com.example.sharemind.postLike.domain;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.postLike.exception.PostLikeErrorCode;
import com.example.sharemind.postLike.exception.PostLikeException;
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
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostLike(Customer customer, Post post) {
        this.customer = customer;
        this.post = post;

        this.post.increaseTotalLike();
    }

    public void updateIsActivatedTrue() {
        checkIsActivatedFalse();

        super.updateIsActivatedTrue();
        this.post.increaseTotalLike();
    }

    public void updateIsActivatedFalse() {
        super.updateIsActivatedFalse();

        this.post.decreaseTotalLike();
    }

    private void checkIsActivatedFalse() {
        if (this.isActivated()) {
            throw new PostLikeException(PostLikeErrorCode.POST_ALREADY_LIKED);
        }
    }
}
