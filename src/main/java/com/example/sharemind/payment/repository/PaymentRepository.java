package com.example.sharemind.payment.repository;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import com.example.sharemind.payment.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p JOIN FETCH p.consult c " +
            "WHERE c.customer = :customer AND p.customerStatus = :status AND p.isActivated = true " +
            "ORDER BY p.paymentId DESC")
    Page<Payment> findAllByCustomerAndCustomerStatusAndIsActivatedIsTrue(
            Customer customer, PaymentCustomerStatus status, Pageable pageable);

    @Query("SELECT p FROM Payment p JOIN FETCH p.consult c " +
            "WHERE p.paymentId < :paymentId AND c.customer = :customer AND p.customerStatus = :status AND p.isActivated = true " +
            "ORDER BY p.paymentId DESC")
    Page<Payment> findAllByPaymentIdLessThanAndCustomerAndCustomerStatusAndIsActivatedIsTrue(
            Long paymentId, Customer customer, PaymentCustomerStatus status, Pageable pageable);
}
