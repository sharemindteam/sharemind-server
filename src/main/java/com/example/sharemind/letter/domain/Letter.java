package com.example.sharemind.letter.domain;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.nonRealtimeMessage.content.LetterMessageType;
import com.example.sharemind.nonRealtimeMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.nonRealtimeMessage.exception.LetterMessageException;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Long letterId;

    @Column(name = "consult_category")
    @Enumerated(EnumType.STRING)
    private ConsultCategory consultCategory;

    @Column(name = "consult_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LetterStatus consultStatus;

    @OneToOne(mappedBy = "letter")
    private Consult consult;

    @Builder
    public Letter() {
        this.consultStatus = LetterStatus.WAITING;
    }

    public void setConsult(Consult consult) {
        this.consult = consult;
    }

    public void checkAuthority(LetterMessageType messageType, Customer customer) {
        switch (messageType) {
            case FIRST_QUESTION, SECOND_QUESTION -> {
                if (!this.consult.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
                    throw new LetterMessageException(LetterMessageErrorCode.MESSAGE_MODIFY_DENIED);
                }
            }
            case FIRST_REPLY, SECOND_REPLY -> {
                if ((customer.getCounselor() == null) || (!this.consult.getCounselor().getCounselorId().equals(customer.getCounselor().getCounselorId()))) {
                    throw new LetterMessageException(LetterMessageErrorCode.MESSAGE_MODIFY_DENIED);
                }
            }
        }
    }
}
