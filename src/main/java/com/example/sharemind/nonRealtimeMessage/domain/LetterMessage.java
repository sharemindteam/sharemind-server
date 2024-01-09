package com.example.sharemind.nonRealtimeMessage.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.nonRealtimeMessage.content.LetterMessageType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class LetterMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "letter_id")
    private Letter letter;

    @Column(name = "message_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LetterMessageType messageType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Builder
    public LetterMessage(Letter letter, LetterMessageType messageType,
                         String content, Boolean isCompleted) {
        this.letter = letter;
        this.messageType = messageType;
        this.content = content;
        this.isCompleted = isCompleted;
    }
}
