package com.example.sharemind.chatMessage.repository;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findTopByChatOrderByUpdatedAtDesc(Chat chat);

    int countByChatAndMessageIdGreaterThanAndIsCustomer(Chat chat, Long lastReadMessageId, Boolean isCustomer);

    Page<ChatMessage> findByChatAndIsActivatedTrueAndMessageIdLessThanOrderByMessageIdDesc(Chat chat, Long messageId, Pageable pageable);

    Page<ChatMessage> findByChatAndIsActivatedTrueOrderByMessageIdDesc(Chat chat, Pageable pageable);

    ChatMessage findTopByChatAndIsCustomerAndIsActivatedTrueOrderByMessageIdDesc(Chat chat, Boolean isCustomer);

    ChatMessage findByChatAndMessageStatusAndIsActivatedTrue(Chat chat, ChatMessageStatus chatMessageStatus);
}
