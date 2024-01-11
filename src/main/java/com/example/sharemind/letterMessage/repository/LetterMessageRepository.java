package com.example.sharemind.letterMessage.repository;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LetterMessageRepository extends JpaRepository<LetterMessage, Long> {
    Optional<LetterMessage> findByMessageIdAndIsActivatedIsTrue(Long messageId);

    Boolean existsByLetterAndMessageTypeAndIsCompletedIsFalseAndIsActivatedIsTrue(Letter letter, LetterMessageType messageType);

    Optional<LetterMessage> findByLetterAndMessageTypeAndIsCompletedIsFalseAndIsActivatedIsTrue(Letter letter, LetterMessageType messageType);
}
