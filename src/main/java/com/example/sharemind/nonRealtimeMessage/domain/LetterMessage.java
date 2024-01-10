package com.example.sharemind.nonRealtimeMessage.domain;

import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.nonRealtimeMessage.content.LetterMessageType;
import com.example.sharemind.nonRealtimeMessage.exception.LetterMessageErrorCode;
import com.example.sharemind.nonRealtimeMessage.exception.LetterMessageException;
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
        validateMessageType(messageType, letter);

        this.letter = letter;
        this.messageType = messageType;
        this.content = content;
        this.isCompleted = isCompleted;
    }

    public void updateLetterStatus() {
        if (this.isCompleted) {
            this.letter.updateLetterStatus(this.messageType.getLetterStatus());
        }
    }

    private void validateMessageType(LetterMessageType messageType, Letter letter) {
        LetterStatus letterStatus = letter.getLetterStatus();

        Boolean isValid = null;
        switch (messageType) {
            case FIRST_QUESTION -> isValid = letterStatus.equals(LetterStatus.WAITING);
            case FIRST_REPLY -> isValid = letterStatus.equals(LetterStatus.FIRST_ASKING);
            case SECOND_QUESTION -> isValid = letterStatus.equals(LetterStatus.FIRST_ANSWER);
            case SECOND_REPLY -> isValid = letterStatus.equals(LetterStatus.SECOND_ASKING);
        }

        if (!isValid) {
            throw new LetterMessageException(LetterMessageErrorCode.INVALID_LETTER_MESSAGE_TYPE);
        }
    }
}
