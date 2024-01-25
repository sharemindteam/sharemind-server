package com.example.sharemind.letter.domain;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.letter.exception.LetterErrorCode;
import com.example.sharemind.letter.exception.LetterException;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import com.example.sharemind.letterMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.letterMessage.exception.LetterMessageException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
public class Letter extends BaseEntity {
    private static final Long DEADLINE_OFFSET = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Long letterId;

    @Column(name = "consult_category")
    @Enumerated(EnumType.STRING)
    private ConsultCategory consultCategory;

    @Column(name = "letter_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LetterStatus letterStatus;

    @Column(name = "counselor_read_id")
    private Long counselorReadId;

    @Column(name = "customer_read_id")
    private Long customerReadId;

    @Column
    private LocalDateTime deadline;

    @OneToOne(mappedBy = "letter")
    private Consult consult;

    @Builder
    public Letter() {
        this.letterStatus = LetterStatus.WAITING;
    }

    public void setConsult(Consult consult) {
        this.consult = consult;
    }

    public void updateLetterStatusAndReadId(LetterStatus letterStatus, Long messageId) {
        this.letterStatus = letterStatus;

        switch (this.letterStatus) {
            case FIRST_ASKING -> {
                updateCustomerReadId(messageId);
                updateDeadline();
                this.consult.updateConsultStatusOnGoing();
            }
            case SECOND_ASKING -> {
                updateCustomerReadId(messageId);
                updateDeadline();
            }
            case FIRST_ANSWER -> {
                updateCounselorReadId(messageId);
                updateDeadline();
            }
            case SECOND_FINISH -> {
                updateCounselorReadId(messageId);
                this.consult.updateConsultStatusFinish();
            }
        }
    }

    public void updateLetterStatusFirstFinish() {
        this.letterStatus = LetterStatus.FIRST_FINISH;
        this.consult.updateConsultStatusFinish();
    }

    public void updateLetterStatusCounselorCancel() {
        this.letterStatus = LetterStatus.COUNSELOR_CANCEL;
        this.consult.updateConsultStatusCounselorCancel();
    }

    public void updateConsultCategory(ConsultCategory category) {
        validateConsultCategory(category);

        this.consultCategory = category;
    }

    public void updateCounselorReadId(Long counselorReadId) {
        if ((this.counselorReadId == null) || (this.counselorReadId < counselorReadId)) {
            this.counselorReadId = counselorReadId;
        }
    }

    public void updateCustomerReadId(Long customerReadId) {
        if ((this.customerReadId == null) || (this.customerReadId < customerReadId)) {
            this.customerReadId = customerReadId;
        }
    }

    public void checkWriteAuthority(LetterMessageType messageType, Customer customer) {
        switch (messageType) {
            case FIRST_QUESTION, SECOND_QUESTION -> {
                if (!this.consult.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
                    throw new LetterMessageException(LetterMessageErrorCode.MESSAGE_MODIFY_DENIED);
                }
            }
            case FIRST_REPLY, SECOND_REPLY -> {
                if ((customer.getCounselor() == null) || (!this.consult.getCounselor().getCounselorId()
                        .equals(customer.getCounselor().getCounselorId()))) {
                    throw new LetterMessageException(LetterMessageErrorCode.MESSAGE_MODIFY_DENIED);
                }
            }
        }
    }

    public Boolean checkReadAuthority(Customer customer) {
        if (this.consult.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            return true;
        } else if ((customer.getCounselor() != null) && (this.consult.getCounselor().getCounselorId()
                .equals(customer.getCounselor().getCounselorId()))) {
            return false;
        } else {
            throw new LetterMessageException(LetterMessageErrorCode.MESSAGE_ACCESS_DENIED);
        }
    }

    private void updateDeadline() {
        this.deadline = LocalDateTime.now().plusDays(DEADLINE_OFFSET);
    }

    private void validateConsultCategory(ConsultCategory category) {
        if (!this.getConsult().getCounselor().getConsultCategories().contains(category)) {
            throw new LetterException(LetterErrorCode.CONSULT_CATEGORY_MISMATCH, category.name());
        }
    }
}
