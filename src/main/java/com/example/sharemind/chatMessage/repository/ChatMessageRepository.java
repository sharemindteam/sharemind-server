package com.example.sharemind.chatMessage.repository;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findTopByChatOrderByUpdatedAtDesc(Chat chat);

    int countByChatAndMessageIdGreaterThanAndIsCustomer(Chat chat, Long lastReadMessageId, Boolean isCustomer);

    Page<ChatMessage> findByChatAndMessageIdLessThanOrderByMessageIdDesc(Chat chat, Long messageId, Pageable pageable);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chat = :chat AND cm.isCustomer = :isCustomer AND cm.isActivated = true ORDER BY cm.messageId DESC")
    ChatMessage findByChatLatestActiveMessageId(Chat chat, Boolean isCustomer);
}
