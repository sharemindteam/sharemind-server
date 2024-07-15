package com.example.sharemind.consult.domain;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.consult.content.ConsultStatus;
import com.example.sharemind.consult.exception.ConsultErrorCode;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.review.domain.Review;
import com.example.sharemind.customer.domain.Customer;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Consult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consult_id")
    private Long consultId;

    @Column(columnDefinition = "BINARY(16)", updatable = false)
    private UUID consultUuid;

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

    @PrePersist
    public void prePersist() {
        this.consultUuid = UUID.randomUUID();
    }

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
        this.payment.updateCounselorStatusConsultFinish();
    }

    public void updateConsultStatusCounselorCancel() {
        this.consultStatus = ConsultStatus.COUNSELOR_CANCEL;

        if (Objects.requireNonNull(this.consultType) == ConsultType.CHAT) {
            this.chat.updateChatStatus(ChatStatus.COUNSELOR_CANCEL);
        }

        this.payment.updateCustomerStatusRefundWaiting();
    }

    public void updateConsultStatusCustomerCancel() {
        this.consultStatus = ConsultStatus.CUSTOMER_CANCEL;

        switch (this.consultType) {
            case CHAT -> this.chat.updateChatStatus(ChatStatus.CUSTOMER_CANCEL);
            case LETTER -> this.letter.updateLetterStatusCustomerCancel();
        }
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
