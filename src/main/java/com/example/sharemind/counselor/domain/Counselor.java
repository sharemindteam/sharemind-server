package com.example.sharemind.counselor.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.Bank;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultStyle;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Counselor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counselor_id")
    private Long counselorId;

    private String nickname;

    @Column(name = "is_educated")
    private Boolean isEducated;

    @ElementCollection(targetClass = ConsultCategory.class, fetch = FetchType.LAZY)
    @JoinTable(name = "categories", joinColumns = @JoinColumn(name = "counselor_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "consult_categories")
    private Set<ConsultCategory> consultCategories;

    @Column(name = "consult_style")
    private ConsultStyle consultStyle;

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    private Integer level;

    private String account;

    private Bank bank;

    @Column(name = "account_holder")
    private String accountHolder;
}
