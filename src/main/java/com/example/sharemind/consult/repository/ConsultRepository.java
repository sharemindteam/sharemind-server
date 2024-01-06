package com.example.sharemind.consult.repository;

import com.example.sharemind.consult.domain.Consult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsultRepository extends JpaRepository<Consult, Long> {
    @Query("SELECT realtime.realtimeId FROM Consult c JOIN c.realtimeConsult realtime WHERE c.customer.customerId = :customerId")
    List<Long> findRealtimeConsultIdsByCustomerId(@Param("customerId") Long customerId); //todo: 쿼리 최적화 필요
}
