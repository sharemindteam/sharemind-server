package com.example.sharemind.chat.repository;

import com.example.sharemind.chat.domain.Chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatIdAndIsActivatedIsTrue(Long chatId);

    @Query("SELECT c FROM Chat c WHERE (c.chatStatus != 'FINISH') AND (c.chatStatus != 'COUNSELOR_CANCEL') AND (c.chatStatus !=  'CUSTOMER_CANCEL') AND c.chatId IN (SELECT cm.chat.chatId FROM ChatMessage cm GROUP BY cm.chat.chatId ORDER BY MAX(cm.createdAt) DESC)")
    List<Chat> findRecentChatsByLatestMessage(Pageable pageable);
}
