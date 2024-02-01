package com.example.sharemind.payment.domain;

import com.example.sharemind.consult.content.ConsultStatus;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.domain.Settlement;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.payment.content.PaymentCounselorStatus;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import com.example.sharemind.payment.exception.PaymentErrorCode;
import com.example.sharemind.payment.exception.PaymentException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.sharemind.global.constants.Constants.FEE;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(nullable = false)
    private String method;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;

    @Column(name = "payment_counselor_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentCounselorStatus counselorStatus;

    @Column(name = "payment_customer_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentCustomerStatus customerStatus;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @OneToOne(mappedBy = "payment", optional = false)
    private Consult consult;

    @Builder
    public Payment(Consult consult) {
        this.consult = consult;
        this.method = "외부 결제";
        this.isPaid = false;
        updateBothStatusNone();
    }

    public void updateIsPaidTrue() {
        this.isPaid = true;
        updateCustomerStatusPaymentComplete();
    }

    public void updateBothStatusNone() {
        this.customerStatus = PaymentCustomerStatus.NONE;
        this.counselorStatus = PaymentCounselorStatus.NONE;
    }

    public void updateCustomerStatusPaymentComplete() {
        this.customerStatus = PaymentCustomerStatus.PAYMENT_COMPLETE;
    }

    public void updateCustomerStatusRefundWaiting() {
        if (!this.consult.getConsultStatus().equals(ConsultStatus.COUNSELOR_CANCEL)) {
            this.consult.updateConsultStatusCustomerCancel();
        }
        this.customerStatus = PaymentCustomerStatus.REFUND_WAITING;
    }

    public void updateCustomerStatusRefundComplete() {
        this.customerStatus = PaymentCustomerStatus.REFUND_COMPLETE;
    }

    public void updateCounselorStatusConsultFinish() {
        this.counselorStatus = PaymentCounselorStatus.CONSULT_FINISH;
    }

    public void updateCounselorStatusSettlementWaiting() {
        this.counselorStatus = PaymentCounselorStatus.SETTLEMENT_WAITING;
    }

    public void updateCounselorStatusSettlementOngoing() {
        Settlement settlement = this.consult.getCounselor().getSettlement();
        long amount = this.consult.getCost() - FEE;
        if (this.getUpdatedAt().isAfter(LocalDateTime.now().minusWeeks(1))) {
            settlement.updateWaitingWeek(-amount);
        }
        if (this.getUpdatedAt().isAfter(LocalDateTime.now().minusMonths(1))) {
            settlement.updateWaitingMonth(-amount);
        }
        settlement.updateWaitingAll(-amount);
        settlement.updateOngoingWeek(amount);
        settlement.updateOngoingMonth(amount);
        settlement.updateOngoingAll(amount);

        this.counselorStatus = PaymentCounselorStatus.SETTLEMENT_ONGOING;
    }

    public void checkUpdateAuthorityByCustomer(Long customerId) {
        if (!this.consult.getCustomer().getCustomerId().equals(customerId)) {
            throw new PaymentException(PaymentErrorCode.PAYMENT_UPDATE_DENIED);
        }
    }

    public void checkUpdateAuthorityByCounselor(Long counselorId) {
        if (!this.consult.getCounselor().getCounselorId().equals(counselorId)) {
            throw new PaymentException(PaymentErrorCode.PAYMENT_UPDATE_DENIED);
        }
    }
}
