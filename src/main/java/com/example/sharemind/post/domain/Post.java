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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.sharemind.global.constants.Constants.MAX_COMMENTS;

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

    @Size(max = 50, message = "제목은 최대 50자입니다.")
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

    @Column(name = "total_scrap", nullable = false)
    private Long totalScrap;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Builder
    public Post(Customer customer, Long cost, Boolean isPublic) {
        this.customer = customer;
        this.cost = cost;
        this.isPublic = isPublic;
        this.postStatus = PostStatus.WAITING;
        this.totalLike = 0L;
        this.totalComment = 0L;
        this.totalScrap = 0L;
        setIsPaid(isPublic);
    }

    public void updateIsPaid() {
        this.isPaid = true;
    }

    public void updatePostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;

        if (this.postStatus == PostStatus.PROCEEDING) {
            this.publishedAt = LocalDateTime.now();
        }

        if (this.postStatus == PostStatus.COMPLETED || this.postStatus == PostStatus.TIME_OUT) {
            this.finishedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }
    }

    public void increaseTotalLike() {
        this.totalLike++;
    }

    public void decreaseTotalLike() {
        this.totalLike--;
    }

    public void increaseTotalComment() {
        this.totalComment++;
        if (totalComment.equals(MAX_COMMENTS)) {
            this.updatePostStatus(PostStatus.COMPLETED);
        }
    }

    public void increaseTotalScrap() {
        this.totalScrap++;
    }

    public void decreaseTotalScrap() {
        this.totalScrap--;
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
            updatePostStatus(PostStatus.PROCEEDING);
        }
    }

    public Boolean checkOwner(Long customerId) {
        return this.customer.getCustomerId().equals(customerId);
    }

    public void checkReadAuthority(Long customerId) {
        if (!this.isPublic && !this.customer.getCustomerId().equals(customerId)) {
            throw new PostException(PostErrorCode.POST_ACCESS_DENIED);
        }
    }

    public void checkWriteAuthority(Customer customer) {
        if (!customer.getCustomerId().equals(this.customer.getCustomerId())) {
            throw new PostException(PostErrorCode.POST_MODIFY_DENIED);
        }
    }

    public void checkPostProceeding() {
        if (this.postStatus != PostStatus.PROCEEDING) {
            throw new PostException(PostErrorCode.POST_NOT_PROCEEDING, this.getPostId().toString());
        }
    }

    public void checkPostProceedingOrTimeOut() {
        if (this.postStatus != PostStatus.PROCEEDING && this.postStatus != PostStatus.TIME_OUT) {
            throw new PostException(PostErrorCode.POST_NOT_PROCEEDING, this.getPostId().toString());
        }
    }

    private void setIsPaid(Boolean isPublic) {
        if (isPublic) {
            this.isPaid = true;
        } else {
            this.isPaid = false;
        }
    }

    private void checkUpdatability() {
        if (this.isCompleted != null && this.isCompleted.equals(true)) {
            throw new PostException(PostErrorCode.POST_ALREADY_COMPLETED);
        }
    }
}
