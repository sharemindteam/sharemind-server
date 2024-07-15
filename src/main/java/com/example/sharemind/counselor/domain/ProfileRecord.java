package com.example.sharemind.counselor.domain;

import com.example.sharemind.counselor.content.ConsultStyle;
import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProfileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_record_id")
    private Long profileRecordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "counselor_id")
    private Counselor counselor;

    @Column(nullable = false)
    private String nickname;

    @ElementCollection(targetClass = ConsultCost.class, fetch = FetchType.LAZY)
    @JoinTable(name = "consult_costs_record", joinColumns = @JoinColumn(name = "profile_record_id"))
    @Column(name = "consult_costs")
    private Set<ConsultCost> consultCosts;

    @ElementCollection(targetClass = ConsultTime.class, fetch = FetchType.LAZY)
    @JoinTable(name = "consult_times_record", joinColumns = @JoinColumn(name = "profile_record_id"))
    @Column(name = "consult_times")
    private Set<ConsultTime> consultTimes;

    @ElementCollection(targetClass = ConsultType.class, fetch = FetchType.LAZY)
    @JoinTable(name = "consult_types_record", joinColumns = @JoinColumn(name = "profile_record_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "consult_types")
    private Set<ConsultType> consultTypes;

    @ElementCollection(targetClass = ConsultCategory.class, fetch = FetchType.LAZY)
    @JoinTable(name = "consult_categories_record", joinColumns = @JoinColumn(name = "profile_record_id"))
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

    @Column(name = "profile_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus;

    @Builder
    public ProfileRecord(Counselor counselor, String nickname, Set<ConsultCost> consultCosts,
            Set<ConsultTime> consultTimes, Set<ConsultType> consultTypes,
            Set<ConsultCategory> consultCategories, ConsultStyle consultStyle, String experience,
            String introduction, ProfileStatus profileStatus) {
        this.counselor = counselor;
        this.nickname = nickname;
        this.consultCosts = consultCosts;
        this.consultTimes = consultTimes;
        this.consultTypes = consultTypes;
        this.consultCategories = consultCategories;
        this.consultStyle = consultStyle;
        this.experience = experience;
        this.introduction = introduction;
        this.profileStatus = profileStatus;
    }
}
