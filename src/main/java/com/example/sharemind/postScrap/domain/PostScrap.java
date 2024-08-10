package com.example.sharemind.postScrap.domain;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.postScrap.exception.PostScrapErrorCode;
import com.example.sharemind.postScrap.exception.PostScrapException;
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
public class PostScrap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_scrap_id")
    private Long postScrapId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostScrap(Customer customer, Post post) {
        this.customer = customer;
        this.post = post;

        this.post.increaseTotalScrap();
    }

    public void updateIsActivatedTrue() {
        checkIsActivatedFalse();

        super.updateIsActivatedTrue();
        this.post.increaseTotalScrap();
    }

    public void updateIsActivatedFalse() {
        super.updateIsActivatedFalse();

        this.post.decreaseTotalScrap();
    }

    private void checkIsActivatedFalse() {
        if (this.isActivated()) {
            throw new PostScrapException(PostScrapErrorCode.POST_ALREADY_SCRAPPED);
        }
    }
}
