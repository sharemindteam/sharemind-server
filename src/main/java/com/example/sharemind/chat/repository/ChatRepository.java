package com.example.sharemind.chat.repository;

import com.example.sharemind.chat.domain.Chat;

import java.util.Optional;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatIdAndIsActivatedIsTrue(Long chatId);

    @Query("SELECT c FROM Chat c WHERE (c.chatStatus != 'FINISH') AND (c.chatStatus != 'COUNSELOR_CANCEL') AND (c.chatStatus !=  'CUSTOMER_CANCEL') AND c.consult.customer = :customer AND c.chatId IN (SELECT cm.chat.chatId FROM ChatMessage cm GROUP BY cm.chat.chatId ORDER BY MAX(cm.createdAt) DESC)")
    Page<Chat> findRecentChatsByLatestMessageAndCustomer(Pageable pageable, Customer customer);

    @Query("SELECT c FROM Chat c WHERE (c.chatStatus != 'FINISH') AND (c.chatStatus != 'COUNSELOR_CANCEL') AND (c.chatStatus !=  'CUSTOMER_CANCEL') AND c.consult.counselor = :counselor AND c.chatId IN (SELECT cm.chat.chatId FROM ChatMessage cm GROUP BY cm.chat.chatId ORDER BY MAX(cm.createdAt) DESC)")
    Page<Chat> findRecentChatsByLatestMessageAndCounselor(Pageable pageable, Counselor counselor);

    @Query("SELECT c FROM Chat c WHERE c.chatStatus = 'WAITING' AND c.consult.customer = :customer ORDER BY c.updatedAt DESC")
    Page<Chat> findChatsByStatusWaitingAndCustomerOrderByUpdatedAtDesc(Pageable pageable, Customer customer);

    @Query("SELECT c FROM Chat c WHERE c.chatStatus = 'WAITING' AND c.consult.counselor = :counselor ORDER BY c.updatedAt DESC")
    Page<Chat> findChatsByStatusWaitingAndCounselorOrderByUpdatedAtDesc(Pageable pageable, Counselor counselor);

    @Query("SELECT COUNT(c) FROM Chat c WHERE (c.chatStatus != 'FINISH') AND (c.chatStatus != 'COUNSELOR_CANCEL') AND (c.chatStatus != 'CUSTOMER_CANCEL') AND c.consult.customer = :customer")
    Integer countChatsByStatusAndCustomer(Customer customer);
}
