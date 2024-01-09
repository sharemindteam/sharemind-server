package com.example.sharemind.chat.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.chat.content.ChatStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "consult_status")
    @Enumerated(EnumType.STRING)
    private ChatStatus consultStatus;

    @Column(name = "read_id")
    private Long readId;
}
