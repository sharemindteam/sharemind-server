package com.example.sharemind.customer.domain;

import com.example.sharemind.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Experience extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private Long experienceId;

    @Column(name = "post_create", nullable = false)
    private Long postCreate;

    @Column(name = "post_answer", nullable = false)
    private Long postAnswer;

    @Column(name = "post_choose", nullable = false)
    private Long postChoose;

    @Column(name = "post_chosen", nullable = false)
    private Long postChosen;

    @Column(name = "post_popularity_create", nullable = false)
    private Long postPopularityCreate;

    @Column(name = "post_popularity_answer", nullable = false)
    private Long postPopularityAnswer;

    @Column(name = "post_popularity_chosen", nullable = false)
    private Long postPopularityChosen;

    @Column(name = "level", nullable = false)
    private Long level;

    @Column(name = "extra_discount", nullable = false)
    private Double extraDiscount;

    @Builder
    public Experience() {
        this.postCreate = 0L;
        this.postAnswer = 0L;
        this.postChoose = 0L;
        this.postChosen = 0L;
        this.postPopularityCreate = 0L;
        this.postPopularityAnswer = 0L;
        this.postPopularityChosen = 0L;
        this.level = 1L;
        this.extraDiscount = 0.0;
    }
}
