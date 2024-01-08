package com.example.sharemind.nonRealtimeMessage.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.nonRealtimeConsult.domain.NonRealtimeConsult;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class NonRealtimeMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "non_realtime_id")
    private NonRealtimeConsult nonRealtimeConsult;

    @Column(name = "is_customer")
    private Boolean isCustomer;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_completed")
    private Boolean isCompleted;
}
