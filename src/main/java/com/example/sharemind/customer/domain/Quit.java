package com.example.sharemind.customer.domain;

import com.example.sharemind.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Quit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quit_id")
    private Long quitId;

    @Column(name = "short_reason", nullable = false)
    private String shortReason;

    @Size(max = 100, message = "선택사항은 최대 100자입니다.")
    @Column(name = "long_reason")
    private String longReason;

    @Builder
    public Quit(String shortReason, String longReason) {
        this.shortReason = shortReason;
        this.longReason = longReason;
    }
}
