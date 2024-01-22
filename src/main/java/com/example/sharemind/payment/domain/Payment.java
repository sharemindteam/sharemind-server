package com.example.sharemind.payment.domain;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.payment.content.PaymentCounselorStatus;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "payment_counselor_status")
    @Enumerated(EnumType.STRING)
    private PaymentCounselorStatus counselorStatus;

    @Column(name = "payment_customer_status")
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
    }

    public void updateIsPaidTrue() {
        this.isPaid = true;
        updatePaymentCustomerStatus(PaymentCustomerStatus.PAYMENT_COMPLETE);
    }

    public void updatePaymentCustomerStatus(PaymentCustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
    }
}
