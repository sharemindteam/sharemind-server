package com.example.sharemind.customer.repository;

import com.example.sharemind.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Boolean existsByEmailAndIsActivatedIsTrue(String email);
    Optional<Customer> findByEmailAndIsActivatedIsTrue(String email);
    Optional<Customer> findByCustomerIdAndIsActivatedIsTrue(Long id);
}
