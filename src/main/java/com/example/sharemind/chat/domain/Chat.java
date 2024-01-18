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

    @Column(name = "chat_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatStatus chatStatus = ChatStatus.WAITING;

    @Column(name = "customer_read_id", nullable = false)
    private Long customerReadId = 0L;

    @Column(name = "counseolor_read_id", nullable = false)
    private Long counselorReadId = 0L;

    public void updateChatStatus(ChatStatus chatStatus) {
        this.chatStatus = chatStatus;
    }

    public void updateStartedAt() {
        this.startedAt = LocalDateTime.now();
    }

    public void updateCustomerReadId(Long id) {
        this.customerReadId = id;
    }

    public void updateCounselorReadId(Long id) {
        this.counselorReadId = id;
    }

    public static Chat newInstance() {
        return new Chat();
    }
}
