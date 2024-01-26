package com.example.sharemind.payment.application;

import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.payment.dto.response.PaymentGetCustomerResponse;

import java.util.List;

public interface PaymentService {
    Payment getPaymentByPaymentId(Long paymentId);

    List<PaymentGetCustomerResponse> getPaymentsByCustomer(Long paymentId, String status, Long customerId);

    List<Payment> getRefundWaitingPayments();
}
