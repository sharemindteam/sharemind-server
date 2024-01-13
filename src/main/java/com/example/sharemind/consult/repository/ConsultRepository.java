package com.example.sharemind.consult.repository;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultRepository extends JpaRepository<Consult, Long> {
    Optional<Consult> findByConsultIdAndIsActivatedIsTrue(Long consultId);

    List<Consult> findAllByIsPaidIsFalseAndIsActivatedIsTrue();

    @Query("SELECT chat.chatId FROM Consult c JOIN c.chat chat WHERE c.customer.customerId = :customerId AND c.isActivated = true")
    List<Long> findChatIdsByCustomerId(Long customerId); //todo: 쿼리 최적화 필요

    @Query("SELECT chat.chatId FROM Consult c JOIN c.chat chat WHERE c.counselor.counselorId = :counselorId AND c.isActivated = true")
    List<Long> findChatIdsByCounselorId(Long counselorId);

    @Query("SELECT c FROM Consult c WHERE c.customer.customerId = :customerId AND c.consultType = :consultType AND c.isPaid = true AND c.isActivated = true")
    List<Consult> findByCustomerIdAndConsultTypeAndIsPaid(Long customerId, ConsultType consultType);

    @Query("SELECT c FROM Consult c WHERE c.counselor.counselorId = :counselorId AND c.consultType = :consultType AND c.isPaid = true AND c.isActivated = true")
    List<Consult> findByCounselorIdAndConsultTypeAndIsPaid(Long counselorId, ConsultType consultType);

    // TODO 임시
    List<Consult> findAllByCustomerAndConsultType(Customer customer, ConsultType consultType);

    // TODO 임시
    List<Consult> findAllByCounselorAndConsultType(Counselor counselor, ConsultType consultType);
}
