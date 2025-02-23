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

    public void increasePostCreate() {
        this.postCreate++;
        updateLevelAndExtraDiscount();
    }

    public void increasePostAnswer() {
        this.postAnswer++;
        updateLevelAndExtraDiscount();
    }

    private void updateLevelAndExtraDiscount() {
        long total = this.postCreate + this.postAnswer + this.postChoose + this.postChosen
                + this.postPopularityCreate + this.postPopularityAnswer + this.postPopularityChosen;

        if (total == 0) {
            this.level = 1L;
            this.extraDiscount = 0.0;
        } else if (total <= 7) {
            this.level = 2L;
            this.extraDiscount = 0.01;
        } else if (total <= 15) {
            this.level = 3L;
            this.extraDiscount = 0.02;
        } else if (total <= 25) {
            this.level = 4L;
            this.extraDiscount = 0.03;
        } else if (total <= 100) {
            this.level = 5L;
            this.extraDiscount = 0.05;
        } else if (total <= 500) {
            this.level = 6L;
            this.extraDiscount = 0.06;
        } else if (total <= 1500) {
            this.level = 7L;
            this.extraDiscount = 0.07;
        } else if (total <= 4000) {
            this.level = 8L;
            this.extraDiscount = 0.08;
        } else if (total <= 10000) {
            this.level = 9L;
            this.extraDiscount = 0.09;
        } else {
            this.level = 10L;
            this.extraDiscount = 0.1;
        }
    }
}
