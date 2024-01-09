package com.example.sharemind.letter.repository;

import com.example.sharemind.letter.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {
    Optional<Letter> findByLetterIdAndIsActivatedIsTrue(Long letterId);
}
