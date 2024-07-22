package com.example.sharemind.counselor.repository;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.content.ConsultCategory;
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

    Boolean existsByNicknameAndCounselorIdNot(String nickname, Long counselorId);

    Optional<Counselor> findByCounselorIdAndIsActivatedIsTrue(Long id);

    List<Counselor> findAllByIsEducatedIsTrueAndIsActivatedIsTrue();

    @Query("SELECT c FROM Counselor c WHERE  c.profileStatus = 'EVALUATION_COMPLETE' AND c.isActivated = true")
    List<Counselor> findAllByProfileStatusIsEvaluationCompleteAndIsActivatedIsTrue();

    @Query("SELECT c FROM Counselor c WHERE  c.profileStatus = 'EVALUATION_PENDING' AND c.isActivated = true")
    List<Counselor> findAllByProfileStatusIsEvaluationPendingAndIsActivatedIsTrue();

    @Query(value = "SELECT c FROM Counselor c "
            + "JOIN Customer cstm ON c.counselorId = cstm.counselor.counselorId "
            + "WHERE (c.nickname LIKE %:keyword% OR cstm.email LIKE %:keyword%) "
            + "AND c.isActivated = true")
    List<Counselor> findAllByNicknameOrEmail(String keyword);

    @Query("SELECT c FROM Counselor c WHERE (c.nickname LIKE %:word% OR c.experience LIKE %:word% OR c.introduction LIKE %:word%) AND c.level >= 1 AND c.isActivated = true AND c.profileStatus = 'EVALUATION_COMPLETE'")
    Page<Counselor> findByWordAndLevelAndStatus(String word, Pageable pageable);


    @Query("SELECT c FROM Counselor c WHERE :category MEMBER OF c.consultCategories AND c.level >= 1 AND c.isActivated = true AND c.profileStatus = 'EVALUATION_COMPLETE'")
    Page<Counselor> findByConsultCategoryAndLevelAndStatus(ConsultCategory category, Pageable pageable);

    @Query("SELECT c FROM Counselor c WHERE c.level >= 1 AND c.isActivated = true AND c.profileStatus = 'EVALUATION_COMPLETE'")
    Page<Counselor> findByLevelAndStatus(Pageable pageable);
}
