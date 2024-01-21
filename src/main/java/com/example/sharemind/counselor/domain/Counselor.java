package com.example.sharemind.counselor.domain;

import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.Bank;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.counselor.content.ConsultStyle;
import com.example.sharemind.global.content.ConsultType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Entity
public class Counselor extends BaseEntity {
    private static final Integer RETRY_EDUCATION_OFFSET = 1;
    private static final Integer COUNSELOR_BASIC_LEVEL = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counselor_id")
    private Long counselorId;

    @Size(min = 1, max = 8, message = "닉네임은 최대 8자입니다.")
    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(name = "is_educated")
    private Boolean isEducated;

    @Column(name = "retry_education")
    private LocalDateTime retryEducation;

    @Column(name = "profile_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus;

    @ElementCollection(targetClass = ConsultCost.class, fetch = FetchType.LAZY)
    @JoinTable(name = "costs", joinColumns = @JoinColumn(name = "counselor_id"))
    @Column(name = "consult_costs")
    private Set<ConsultCost> consultCosts;

    @ElementCollection(targetClass = ConsultTime.class, fetch = FetchType.LAZY)
    @JoinTable(name = "times", joinColumns = @JoinColumn(name = "counselor_id"))
    @Column(name = "consult_times")
    private Set<ConsultTime> consultTimes;

    @ElementCollection(targetClass = ConsultType.class, fetch = FetchType.LAZY)
    @JoinTable(name = "types", joinColumns = @JoinColumn(name = "counselor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "consult_types")
    private Set<ConsultType> consultTypes;

    @ElementCollection(targetClass = ConsultCategory.class, fetch = FetchType.LAZY)
    @JoinTable(name = "categories", joinColumns = @JoinColumn(name = "counselor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "consult_categories")
    private Set<ConsultCategory> consultCategories;

    @Column(name = "consult_style")
    @Enumerated(EnumType.STRING)
    private ConsultStyle consultStyle;

    @Size(max = 20000, message = "경험 소개는 최대 20000자입니다.")
    @Column(columnDefinition = "TEXT")
    private String experience;

    @Size(max = 50, message = "한줄 소개는 최대 50자입니다.")
    @Column(columnDefinition = "TEXT")
    private String introduction;

    @PositiveOrZero(message = "상담사 레벨은 0 이상입니다.")
    @Column(nullable = false)
    private Integer level;

    private String account;

    @Enumerated(EnumType.STRING)
    private Bank bank;

    @Column(name = "account_holder")
    private String accountHolder;

    @PositiveOrZero(message = "총 리뷰 수는 0 이상입니다.")
    @Column(name = "total_review", nullable = false)
    private Long totalReview;

    @PositiveOrZero(message = "리뷰 평점은 0 이상입니다.")
    @Column(name = "rating_average", nullable = false)
    private Double ratingAverage;

    @Column(name = "profile_updated_at", nullable = false)
    private LocalDateTime profileUpdatedAt;

    @Column(name = "total_consult", nullable = false)
    private Long totalConsult;

    @Builder
    public Counselor() {
        this.nickname = "판매자" + new Random().nextInt(99999);
        this.level = 0;
        this.totalReview = 0L;
        this.ratingAverage = 0.0;
        this.profileStatus = ProfileStatus.NO_PROFILE;
        this.profileUpdatedAt = LocalDateTime.now();
        this.totalConsult = 0L;
    }

    public Long getConsultCost(ConsultType consultType) {
        return this.consultCosts.stream()
                .filter(consultCost -> consultCost.getConsultType().equals(consultType))
                .findAny().orElseThrow(() -> new CounselorException(CounselorErrorCode.COST_NOT_FOUND))
                .getCost();
    }

    public void updateProfile(String nickname, Set<ConsultCategory> consultCategories, ConsultStyle consultStyle,
                              Set<ConsultType> consultTypes, Set<ConsultTime> consultTimes,
                              Set<ConsultCost> consultCosts,
                              String introduction, String experience) {
        this.nickname = nickname;
        this.consultCategories = consultCategories;
        this.consultStyle = consultStyle;
        this.consultTypes = consultTypes;
        this.consultTimes = consultTimes;
        this.consultCosts = consultCosts;
        this.introduction = introduction;
        this.experience = experience;

        this.profileStatus = ProfileStatus.EVALUATION_PENDING;
    }

    public void updateProfileStatusAndProfileUpdatedAt(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
        this.profileUpdatedAt = LocalDateTime.now();
    }

    public void increaseTotalConsult() {
        this.totalConsult += 1;
    }

    public void updateIsEducated(Boolean isEducated) {
        validateIsEducated();
        this.isEducated = isEducated;

        if (isEducated.equals(false)) {
            this.retryEducation = LocalDateTime.now().plusDays(RETRY_EDUCATION_OFFSET);
        } else {
            this.level = COUNSELOR_BASIC_LEVEL;
        }
    }

    private void validateIsEducated() {
        if ((this.isEducated != null) && (this.isEducated.equals(true))) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_ALREADY_EDUCATED);
        }
    }
}
