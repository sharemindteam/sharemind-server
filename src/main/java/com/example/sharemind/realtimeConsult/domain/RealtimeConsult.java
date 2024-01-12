package com.example.sharemind.realtimeConsult.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.realtimeConsult.content.RealtimeConsultStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RealtimeConsult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "realtime_id")
    private Long realtimeId;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "consult_status")
    private RealtimeConsultStatus consultStatus;

    @Column(name = "read_id")
    private Long readId;
}
