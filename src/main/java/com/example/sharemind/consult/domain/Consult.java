package com.example.sharemind.consult.domain;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.global.content.RefundStatus;
import com.example.sharemind.nonRealtimeConsult.domain.NonRealtimeConsult;
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

    private Long cost;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "refund_status")
    private RefundStatus refundStatus;

    @Column(name = "type")
    private ConsultType consultType;

    @OneToOne
    @JoinColumn(name = "review_id", unique = true)
    private Review review;

    @OneToOne
    @JoinColumn(name = "non_realtime_id", unique = true)
    private NonRealtimeConsult nonRealtimeConsult;

    @OneToOne
    @JoinColumn(name = "realtime_id", unique = true)
    private RealtimeConsult realtimeConsult;
}
