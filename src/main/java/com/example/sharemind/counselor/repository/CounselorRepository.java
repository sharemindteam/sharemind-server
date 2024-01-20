package com.example.sharemind.counselor.repository;

import com.example.sharemind.counselor.domain.Counselor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    Boolean existsByNickname(String nickname);

    Optional<Counselor> findByCounselorIdAndIsActivatedIsTrue(Long id);

    @Query("SELECT c FROM Counselor c WHERE  c.profileStatus = 'EVALUATION_PENDING' AND c.isActivated = true")
    List<Counselor> findAllByProfileStatusIsEvaluationPendingAndIsActivatedIsTrue();

    @Query("SELECT c FROM Counselor c WHERE (c.nickname LIKE %:word% OR c.experience LIKE %:word% OR c.introduction LIKE %:word%) AND c.level >= 1 AND c.isActivated = true AND c.profileStatus = 'EVALUATION_COMPLETE'")
    Page<Counselor> findByWordAndLevelAndStatus(String word, Pageable pageable);
}
