package com.example.sharemind.post.domain;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.post.content.PostStatus;
import com.example.sharemind.post.exception.PostErrorCode;
import com.example.sharemind.post.exception.PostException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "consult_category")
    @Enumerated(EnumType.STRING)
    private ConsultCategory consultCategory;

    private String title;

    @Size(max = 1000, message = "상담 내용은 최대 1000자입니다.")
    private String content;

    @Column(nullable = false)
    private Long cost;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "post_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Column(name = "total_like", nullable = false)
    private Long totalLike;

    @Column(name = "total_comment", nullable = false)
    private Long totalComment;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Builder
    public Post(Customer customer, Long cost, Boolean isPublic) {
        this.customer = customer;
        this.cost = cost;
        this.isPublic = isPublic;
        this.postStatus = PostStatus.WAITING;
        this.totalLike = 0L;
        this.totalComment = 0L;
        setIsPaid(isPublic);
    }

    public void updateIsPaid() {
        this.isPaid = true;
    }

    public void updatePost(ConsultCategory consultCategory, String title, String content,
            Boolean isCompleted, Customer customer) {
        checkUpdatability();
        checkWriteAuthority(customer);

        this.consultCategory = consultCategory;
        this.title = title;
        this.content = content;
        this.isCompleted = isCompleted;

        if (isCompleted) {
            this.postStatus = PostStatus.PROCEEDING;
        }
    }

    private void setIsPaid(Boolean isPublic) {
        if (isPublic) {
            this.isPaid = true;
        } else {
            this.isPaid = false;
        }
    }

    private void checkWriteAuthority(Customer customer) {
        if (!customer.getCustomerId().equals(this.customer.getCustomerId())) {
            throw new PostException(PostErrorCode.POST_MODIFY_DENIED);
        }
    }

    private void checkUpdatability() {
        if (this.isCompleted.equals(true)) {
            throw new PostException(PostErrorCode.POST_ALREADY_COMPLETED);
        }
    }
}
