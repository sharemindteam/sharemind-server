package com.example.sharemind.chatMessage.domain;

import com.example.sharemind.chatMessage.content.ChatMessageStatus;
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

    @Column(name = "is_customer", nullable = false)
    private Boolean isCustomer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "message_Status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatMessageStatus messageStatus;

    @Builder
    public ChatMessage(Chat chat, Boolean isCustomer, String content, ChatMessageStatus messageStatus) {
        this.chat = chat;
        this.isCustomer = isCustomer;
        this.content = content;
        this.messageStatus = messageStatus;
    }
}
