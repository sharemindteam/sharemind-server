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
public class Level extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Long levelId;

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

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "extra_discount", nullable = false)
    private Double extraDiscount;

    @Builder
    public Level() {
        this.postCreate = 0L;
        this.postAnswer = 0L;
        this.postChoose = 0L;
        this.postChosen = 0L;
        this.postPopularityCreate = 0L;
        this.postPopularityAnswer = 0L;
        this.postPopularityChosen = 0L;
        this.grade = 1;
        this.extraDiscount = 0.0;
    }

    public void increasePostCreate() {
        this.postCreate++;
        updateGradeAndExtraDiscount();
    }

    public void increasePostAnswer() {
        this.postAnswer++;
        updateGradeAndExtraDiscount();
    }

    public void increasePostChoose() {
        this.postChoose++;
        updateGradeAndExtraDiscount();
    }

    public void increasePostChosen() {
        this.postChosen += 3;
        updateGradeAndExtraDiscount();
    }

    public void increasePostPopularityCreate() {
        this.postPopularityCreate += 3;
        updateGradeAndExtraDiscount();
    }

    public void increasePostPopularityAnswer() {
        this.postPopularityAnswer += 2;
        updateGradeAndExtraDiscount();
    }

    public void increasePostPopularityChosen() {
        this.postPopularityChosen += 3;
        updateGradeAndExtraDiscount();
    }

    private void updateGradeAndExtraDiscount() {
        long total = this.postCreate + this.postAnswer + this.postChoose + this.postChosen
                + this.postPopularityCreate + this.postPopularityAnswer + this.postPopularityChosen;

        if (total == 0) {
            this.grade = 1;
            this.extraDiscount = 0.0;
        } else if (total <= 7) {
            this.grade = 2;
            this.extraDiscount = 0.01;
        } else if (total <= 15) {
            this.grade = 3;
            this.extraDiscount = 0.02;
        } else if (total <= 25) {
            this.grade = 4;
            this.extraDiscount = 0.03;
        } else if (total <= 100) {
            this.grade = 5;
            this.extraDiscount = 0.05;
        } else if (total <= 500) {
            this.grade = 6;
            this.extraDiscount = 0.06;
        } else if (total <= 1500) {
            this.grade = 7;
            this.extraDiscount = 0.07;
        } else if (total <= 4000) {
            this.grade = 8;
            this.extraDiscount = 0.08;
        } else if (total <= 10000) {
            this.grade = 9;
            this.extraDiscount = 0.09;
        } else {
            this.grade = 10;
            this.extraDiscount = 0.1;
        }
    }
}
