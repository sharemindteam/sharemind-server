package com.example.sharemind.consult.domain;

import com.example.sharemind.consult.exception.ConsultErrorCode;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.consult.content.RefundStatus;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.realtimeConsult.domain.RealtimeConsult;
import com.example.sharemind.review.domain.Review;
import com.example.sharemind.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Consult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consult_id")
    private Long consultId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "counselor_id")
    private Counselor counselor;

    @Column(nullable = false)
    private Long cost;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;

    @Column(name = "refund_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultType consultType;

    @OneToOne
    @JoinColumn(name = "review_id", unique = true)
    private Review review;

    @OneToOne
    @JoinColumn(name = "letter_id", unique = true)
    private Letter letter;

    @OneToOne
    @JoinColumn(name = "realtime_id", unique = true)
    private RealtimeConsult realtimeConsult;

    @Builder
    public Consult(Customer customer, Counselor counselor, Long cost, ConsultType consultType) {
        this.customer = customer;
        this.counselor = counselor;
        this.cost = cost;
        this.consultType = consultType;

        this.isPaid = false;
        this.refundStatus = RefundStatus.NO_REFUND;
    }

    public void updateIsPaidAndLetter(Letter letter) {
        validateLetter();

        this.isPaid = true;
        setLetter(letter);
    }

    private void validateLetter() {
        if (!this.consultType.equals(ConsultType.LETTER)) {
            throw new ConsultException(ConsultErrorCode.CONSULT_TYPE_MISMATCH);
        }
    }

    private void setLetter(Letter letter) {
        this.letter = letter;
        letter.setConsult(this);
    }
}
