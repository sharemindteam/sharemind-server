package com.example.sharemind.chatMessage.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.chat.domain.Chat;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "is_customer")
    private Boolean isCustomer;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public ChatMessage(Chat chat, Boolean isCustomer, String content) {
        this.chat = chat;
        this.isCustomer = isCustomer;
        this.content = content;
    }
}
