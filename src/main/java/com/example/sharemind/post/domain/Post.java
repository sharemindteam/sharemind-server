package com.example.sharemind.post.domain;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.post.content.PostStatus;
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

    @Column(name = "consult_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultCategory consultCategory;

    @Column(nullable = false)
    private String title;

    @Size(max = 1000, message = "상담 내용은 최대 1000자입니다.")
    @Column(nullable = false)
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

    @Builder
    public Post(Customer customer, ConsultCategory consultCategory, String title, String content,
            Long cost, Boolean isPublic) {
        this.customer = customer;
        this.consultCategory = consultCategory;
        this.title = title;
        this.content = content;
        this.cost = cost;
        this.isPublic = isPublic;
        this.postStatus = PostStatus.PROCEEDING;
        this.totalLike = 0L;
        this.totalComment = 0L;
        setIsPaid(isPublic);
    }

    private void setIsPaid(Boolean isPublic) {
        if (isPublic) {
            this.isPaid = true;
        } else {
            this.isPaid = false;
        }
    }
}
