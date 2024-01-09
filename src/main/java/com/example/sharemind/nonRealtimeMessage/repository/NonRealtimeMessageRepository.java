package com.example.sharemind.nonRealtimeMessage.repository;

import com.example.sharemind.nonRealtimeMessage.domain.NonRealtimeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonRealtimeMessageRepository extends JpaRepository<NonRealtimeMessage, Long> {
}
