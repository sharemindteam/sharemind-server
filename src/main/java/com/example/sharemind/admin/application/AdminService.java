package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultGetUnpaidResponse;
import com.example.sharemind.counselor.dto.response.CounselorGetProfileResponse;
import com.example.sharemind.payment.dto.response.PaymentGetCustomerResponse;

import java.util.List;

public interface AdminService {
    List<ConsultGetUnpaidResponse> getUnpaidConsults();

    void updateIsPaid(Long consultId);

    List<CounselorGetProfileResponse> getPendingCounselors();

    void updateProfileStatus(Long counselorId, Boolean isPassed);

    List<PaymentGetCustomerResponse> getRefundWaitingPayments();

    void updatePaymentCustomerStatus(Long paymentId);
}
