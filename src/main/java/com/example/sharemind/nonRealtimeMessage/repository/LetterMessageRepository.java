package com.example.sharemind.nonRealtimeMessage.repository;

import com.example.sharemind.nonRealtimeMessage.domain.LetterMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterMessageRepository extends JpaRepository<LetterMessage, Long> {
}
