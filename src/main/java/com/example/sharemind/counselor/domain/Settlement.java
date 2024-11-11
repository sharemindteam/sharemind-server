package com.example.sharemind.counselor.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_id")
    private Long settlementId;

    @Column(name = "waiting_week", nullable = false)
    private Long waitingWeek;

    @Column(name = "waiting_month", nullable = false)
    private Long waitingMonth;

    @Column(name = "waiting_all", nullable = false)
    private Long waitingAll;

    @Column(name = "ongoing_week", nullable = false)
    private Long ongoingWeek;

    @Column(name = "ongoing_month", nullable = false)
    private Long ongoingMonth;

    @Column(name = "ongoing_all", nullable = false)
    private Long ongoingAll;

    @Column(name = "complete_week", nullable = false)
    private Long completeWeek;

    @Column(name = "complete_month", nullable = false)
    private Long completeMonth;

    @Column(name = "complete_all", nullable = false)
    private Long completeAll;

    @Builder
    public Settlement() {
        this.waitingWeek = 0L;
        this.waitingMonth = 0L;
        this.waitingAll = 0L;
        this.ongoingWeek = 0L;
        this.ongoingMonth = 0L;
        this.ongoingAll = 0L;
        this.completeWeek = 0L;
        this.completeMonth = 0L;
        this.completeAll = 0L;
    }

    public void updateWaitingWeek(Long amount) {
        this.waitingWeek += amount;
    }

    public void updateWaitingMonth(Long amount) {
        this.waitingMonth += amount;
    }

    public void updateWaitingAll(Long amount) {
        this.waitingAll += amount;
    }

    public void updateOngoingWeek(Long amount) {
        this.ongoingWeek += amount;
    }

    public void updateOngoingMonth(Long amount) {
        this.ongoingMonth += amount;
    }

    public void updateOngoingAll(Long amount) {
        this.ongoingAll += amount;
    }

    public void updateCompleteWeek(Long amount) {
        this.completeWeek += amount;
    }

    public void updateCompleteMonth(Long amount) {
        this.completeMonth += amount;
    }

    public void updateCompleteAll(Long amount) {
        this.completeAll += amount;
    }

    public void clearAll() {
        this.waitingWeek = 0L;
        this.waitingMonth = 0L;
        this.waitingAll = 0L;
        this.ongoingWeek = 0L;
        this.ongoingMonth = 0L;
        this.ongoingAll = 0L;
        this.completeWeek = 0L;
        this.completeMonth = 0L;
        this.completeAll = 0L;
    }
}
