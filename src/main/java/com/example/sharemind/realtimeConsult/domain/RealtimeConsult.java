package com.example.sharemind.realtimeConsult.domain;

import com.example.sharemind.global.common.BaseEntity;
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

    @Column(name = "is_finished")
    private Boolean isFinished;

    @Column(name = "read_id")
    private Long readId;
}
