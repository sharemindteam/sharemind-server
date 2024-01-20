package com.example.sharemind.searchWord.repository;

import com.example.sharemind.searchWord.domain.SearchWord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchWordRepository extends JpaRepository<SearchWord, Long> {
    Optional<SearchWord> findByWordAndIsActivatedTrue(String word);
}
