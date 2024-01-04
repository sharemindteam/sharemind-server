package com.example.sharemind.nonRealtimeConsult.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.nonRealtimeConsult.content.NonRealtimeConsultStatus;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class NonRealtimeConsult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "non_realtime_id")
    private Long nonRealtimeId;

    @Column(name = "consult_category")
    private ConsultCategory consultCategory;

    @Column(name = "read_id")
    private Long readId;

    @Column(name = "consult_status")
    private NonRealtimeConsultStatus consultStatus;
}
