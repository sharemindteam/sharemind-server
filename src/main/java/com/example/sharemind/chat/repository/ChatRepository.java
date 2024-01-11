package com.example.sharemind.chat.repository;

import com.example.sharemind.chat.domain.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatIdAndIsActivatedIsTrue(Long chatId);
}
