package com.example.sharemind.nonRealtimeMessage.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.nonRealtimeConsult.domain.NonRealtimeConsult;
import com.example.sharemind.nonRealtimeMessage.content.NonRealtimeMessageType;
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

    @Column(name = "message_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NonRealtimeMessageType messageType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Builder
    public NonRealtimeMessage(NonRealtimeConsult nonRealtimeConsult, NonRealtimeMessageType messageType,
                              String content, Boolean isCompleted) {
        this.nonRealtimeConsult = nonRealtimeConsult;
        this.messageType = messageType;
        this.content = content;
        this.isCompleted = isCompleted;
    }
}
