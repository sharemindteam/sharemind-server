package com.example.sharemind.payment.application;

import com.example.sharemind.payment.dto.response.PaymentGetCustomerResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentGetCustomerResponse> getPaymentsByCustomer(Long paymentId, String status, Long customerId);
}
