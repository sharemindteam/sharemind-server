package com.example.sharemind.review.domain;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @OneToOne(mappedBy = "review", optional = false)
    private Consult consult;

    @Builder
    public Review(Consult consult) {
        this.consult = consult;
        this.isCompleted = false;
    }
}
