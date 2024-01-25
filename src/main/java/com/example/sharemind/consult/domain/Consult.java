package com.example.sharemind.consult.domain;

import com.example.sharemind.consult.content.ConsultStatus;
import com.example.sharemind.consult.exception.ConsultErrorCode;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.review.domain.Review;
import com.example.sharemind.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultType consultType;

    @Column(name = "consult_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultStatus consultStatus;

    @Column(name = "consulted_at")
    private LocalDateTime consultedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "review_id", unique = true)
    private Review review;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_id", unique = true)
    private Letter letter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", unique = true)
    private Chat chat;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;

    @Builder
    public Consult(Customer customer, Counselor counselor, Long cost, ConsultType consultType) {
        this.customer = customer;
        this.counselor = counselor;
        this.cost = cost;
        this.consultType = consultType;
        this.consultStatus = ConsultStatus.WAITING;
        this.payment = Payment.builder().consult(this).build();
    }

    public void updateConsultStatusOnGoing() {
        this.consultStatus = ConsultStatus.ONGOING;
        updateConsultedAt();
    }

    public void updateConsultStatusFinish() {
        this.consultStatus = ConsultStatus.FINISH;
        setReview();

        this.counselor.increaseTotalConsult();
    }

    public void updateConsultStatusCounselorCancel() {
        this.consultStatus = ConsultStatus.COUNSELOR_CANCEL;

        this.payment.updatePaymentCustomerStatus(PaymentCustomerStatus.REFUND_WAITING);
    }

    public void updateIsPaidAndLetter(Letter letter) {
        validateConsultType(ConsultType.LETTER);
        setLetter(letter);
        updateConsultedAt();

        this.payment.updateIsPaidTrue();
    }

    public void updateIsPaidAndChat(Chat chat) {
        validateConsultType(ConsultType.CHAT);
        setChat(chat);
        updateConsultedAt();

        this.payment.updateIsPaidTrue();
    }

    public void setReview() {
        this.review = Review.builder().consult(this).build();
    }

    private void validateConsultType(ConsultType consultType) {
        if (!this.consultType.equals(consultType)) {
            throw new ConsultException(ConsultErrorCode.CONSULT_TYPE_MISMATCH);
        }
    }

    private void updateConsultedAt() {
        this.consultedAt = LocalDateTime.now();
    }

    private void setLetter(Letter letter) {
        this.letter = letter;
        letter.setConsult(this);
    }

    private void setChat(Chat chat) {
        this.chat = chat;
        chat.setConsult(this);
    }
}
