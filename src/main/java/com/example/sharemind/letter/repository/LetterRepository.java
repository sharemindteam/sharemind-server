package com.example.sharemind.letter.repository;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letter.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {
    Optional<Letter> findByLetterIdAndIsActivatedIsTrue(Long letterId);

    @Query("SELECT l FROM Letter l JOIN FETCH l.consult c " +
            "WHERE c.customer = :customer AND " +
            "(l.letterStatus = 'WAITING' OR l.letterStatus = 'FIRST_ASKING' OR l.letterStatus = 'FIRST_ANSWER' OR " +
            "l.letterStatus = 'SECOND_ASKING') AND l.isActivated = true " +
            "ORDER BY l.updatedAt DESC")
    List<Letter> findAllByConsultCustomerAndLetterStatusOngoingOrderByUpdatedAtDesc(Customer customer);

    @Query("SELECT l FROM Letter l JOIN FETCH l.consult c " +
            "WHERE c.counselor = :counselor AND " +
            "(l.letterStatus = 'WAITING' OR l.letterStatus = 'FIRST_ASKING' OR l.letterStatus = 'FIRST_ANSWER' OR " +
            "l.letterStatus = 'SECOND_ASKING') AND l.isActivated = true " +
            "ORDER BY l.updatedAt DESC")
    List<Letter> findAllByConsultCounselorAndLetterStatusOngoingOrderByUpdatedAtDesc(Counselor counselor);
}
