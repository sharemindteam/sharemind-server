package com.example.sharemind.letterMessage.repository;

import com.example.sharemind.letterMessage.domain.LetterMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterMessageRepository extends JpaRepository<LetterMessage, Long> {
}
