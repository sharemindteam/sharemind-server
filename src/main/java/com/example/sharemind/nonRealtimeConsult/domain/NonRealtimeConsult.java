package com.example.sharemind.nonRealtimeConsult.domain;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.nonRealtimeConsult.content.NonRealtimeConsultStatus;
import com.example.sharemind.nonRealtimeMessage.domain.NonRealtimeMessageType;
import com.example.sharemind.nonRealtimeMessage.exception.NonRealtimeMessageErrorCode;
import com.example.sharemind.nonRealtimeMessage.exception.NonRealtimeMessageException;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
public class NonRealtimeConsult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "non_realtime_id")
    private Long nonRealtimeId;

    @Column(name = "consult_category")
    @Enumerated(EnumType.STRING)
    private ConsultCategory consultCategory;

    @Column(name = "consult_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NonRealtimeConsultStatus consultStatus;

    @OneToOne(mappedBy = "nonRealtimeConsult")
    private Consult consult;

    @Builder
    public NonRealtimeConsult() {
        this.consultStatus = NonRealtimeConsultStatus.WAITING;
    }

    public void setConsult(Consult consult) {
        this.consult = consult;
    }
}
