package com.example.sharemind.counselor.domain;

import com.example.sharemind.counselor.content.ConsultCost;
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
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Counselor extends BaseEntity {
    private static final Integer RETRY_EDUCATION_OFFSET = 1;
    private static final Integer COUNSELOR_BASIC_LEVEL = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counselor_id")
    private Long counselorId;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "is_educated", nullable = false)
    private Boolean isEducated;

    @Column(name = "retry_education")
    private LocalDateTime retryEducation;

    @ElementCollection(targetClass = ConsultCost.class, fetch = FetchType.LAZY)
    @JoinTable(name = "costs", joinColumns = @JoinColumn(name = "counselor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "consult_costs")
    private Set<ConsultCost> consultCosts;

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

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(nullable = false)
    private Integer level;

    private String account;

    @Enumerated(EnumType.STRING)
    private Bank bank;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "total_review", nullable = false)
    private Long totalReview;

    @Column(name = "rating_average", nullable = false)
    private Double ratingAverage;

    @Builder
    public Counselor(Boolean isEducated) {
        this.isEducated = isEducated;

        this.nickname = "판매자" + new Random().nextInt(999999);
        this.level = 0;
        this.totalReview = 0L;
        this.ratingAverage = 0.0;
    }

    public Long getConsultCost(ConsultType consultType) {
        return this.consultCosts.stream()
                .filter(consultCost -> consultCost.getConsultType().equals(consultType))
                .findAny().orElseThrow(() -> new CounselorException(CounselorErrorCode.COST_NOT_FOUND))
                .getCost();
    }

    public void updateIsEducated(Boolean isEducated) {
        this.isEducated = isEducated;

        if (isEducated.equals(false)) {
            this.retryEducation = LocalDateTime.now().plusDays(RETRY_EDUCATION_OFFSET);
        } else {
            this.level = COUNSELOR_BASIC_LEVEL;
        }
    }
}
