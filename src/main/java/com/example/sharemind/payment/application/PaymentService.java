package com.example.sharemind.payment.application;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.payment.dto.response.PaymentGetCounselorHomeResponse;
import com.example.sharemind.payment.dto.response.PaymentGetCounselorResponses;
import com.example.sharemind.payment.dto.response.PaymentGetCustomerResponse;

import java.util.List;

public interface PaymentService {
    Payment getPaymentByPaymentId(Long paymentId);

    Payment getPaymentByPayAppId(String payAppId);

    List<PaymentGetCustomerResponse> getPaymentsByCustomer(Long paymentId, String status, Long customerId);

    void updateRefundWaitingByCustomer(Long paymentId, Long customerId);

    List<Payment> getRefundWaitingPayments();

    PaymentGetCounselorResponses getPaymentsByCounselor(Long paymentId, String status, String sort, Long customerId);

    void updateSettlementOngoingByCounselor(Long paymentId, Long customerId);

    PaymentGetCounselorHomeResponse getCounselorHomePayment(Long customerId);

    List<Payment> getSettlementOngoingPayments();

    Boolean checkRefundWaitingExists(Customer customer);

    Boolean checkNotSettlementCompleteAndNotNoneExists(Counselor counselor);
}
