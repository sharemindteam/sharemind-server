package com.example.sharemind.customer.repository;

import com.example.sharemind.customer.domain.Quit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuitRepository extends JpaRepository<Quit, Long> {
}
