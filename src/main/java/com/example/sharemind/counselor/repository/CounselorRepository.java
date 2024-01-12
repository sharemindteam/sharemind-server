package com.example.sharemind.counselor.repository;

import com.example.sharemind.counselor.domain.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    Optional<Counselor> findByCounselorIdAndIsActivatedIsTrue(Long id);
}
