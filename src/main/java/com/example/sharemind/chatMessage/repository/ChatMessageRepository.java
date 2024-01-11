package com.example.sharemind.chatMessage.repository;

import com.example.sharemind.chatMessage.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
