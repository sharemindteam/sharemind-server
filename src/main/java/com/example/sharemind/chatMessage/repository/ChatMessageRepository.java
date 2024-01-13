package com.example.sharemind.chatMessage.repository;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findTopByChatOrderByUpdatedAtDesc(Chat chat);

    int countByChatAndMessageIdGreaterThanAndIsCustomer(Chat chat, Long lastReadMessageId, Boolean isCustomer);
}
