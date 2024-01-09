package com.example.sharemind.consult.repository;

import com.example.sharemind.consult.domain.Consult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultRepository extends JpaRepository<Consult, Long> {
    Optional<Consult> findByConsultIdAndIsActivatedIsTrue(Long consultId);

    List<Consult> findAllByIsPaidIsFalseAndIsActivatedIsTrue();

    @Query("SELECT chat.chatId FROM Consult c JOIN c.chat chat WHERE c.customer.customerId = :customerId")
    List<Long> findChatIdsByCustomerId(Long customerId); //todo: 쿼리 최적화 필요

    @Query("SELECT chat.chatId FROM Consult c JOIN c.chat chat WHERE c.counselor.counselorId = :counselorId")
    List<Long> findChatIdsByCounselorId(Long counselorId);
}
