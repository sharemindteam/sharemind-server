package com.example.sharemind.nonRealtimeConsult.repository;

import com.example.sharemind.nonRealtimeConsult.domain.NonRealtimeConsult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonRealtimeConsultRepository extends JpaRepository<NonRealtimeConsult, Long> {
}
