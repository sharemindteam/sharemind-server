package com.example.sharemind.customer.repository;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Boolean existsByEmailAndIsActivatedIsTrue(String email);

    Optional<Customer> findByEmailAndIsActivatedIsTrue(String email);

    Optional<Customer> findByCustomerIdAndIsActivatedIsTrue(Long id);

    Optional<Customer> findByCounselorAndIsActivatedIsTrue(Counselor counselor);

    @Query(value = "SELECT * FROM customer "
            + "WHERE (nickname LIKE %:keyword% OR email LIKE %:keyword%) "
            + "AND is_activated = true", nativeQuery = true)
    List<Customer> findAllByNicknameOrEmail(String keyword);

    Long countAllByIsActivatedIsTrue();
}
