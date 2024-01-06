package com.example.sharemind.consult.repository;

import com.example.sharemind.consult.domain.Consult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultRepository extends JpaRepository<Consult, Long> {
    Optional<Consult> findByConsultIdAndIsActivatedIsTrue(Long consultId);
    List<Consult> findAllByIsPaidIsFalseAndIsActivatedIsTrue();
}
