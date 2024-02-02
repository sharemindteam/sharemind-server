package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.PaymentGetRefundWaitingResponse;
import com.example.sharemind.admin.dto.response.PaymentGetSettlementOngoingResponse;
import com.example.sharemind.counselor.dto.response.CounselorGetProfileResponse;

import java.util.List;

public interface AdminService {
    List<ConsultGetUnpaidResponse> getUnpaidConsults();

    void updateIsPaid(Long consultId);

    List<CounselorGetProfileResponse> getPendingCounselors();

    void updateProfileStatus(Long counselorId, Boolean isPassed);

    List<PaymentGetRefundWaitingResponse> getRefundWaitingPayments();

    void updateRefundComplete(Long paymentId);

    List<PaymentGetSettlementOngoingResponse> getSettlementOngoingPayments();

    void updateSettlementComplete(Long paymentId);
}
