package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.CounselorGetByNicknameOrEmailResponse;
import com.example.sharemind.admin.dto.response.CustomerGetByNicknameOrEmailResponse;
import com.example.sharemind.admin.dto.response.InformationGetResponse;
import com.example.sharemind.admin.dto.response.PaymentGetRefundWaitingResponse;
import com.example.sharemind.admin.dto.response.PaymentGetSettlementOngoingResponse;
import com.example.sharemind.admin.dto.response.PostGetByUuidResponse;
import com.example.sharemind.admin.dto.response.PostGetUnpaidPrivateResponse;
import com.example.sharemind.counselor.dto.response.CounselorGetProfileResponse;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    List<ConsultGetUnpaidResponse> getUnpaidConsults();

    void updateConsultIsPaid(Long consultId);

    List<CounselorGetProfileResponse> getPendingCounselors();

    void updateProfileStatus(Long counselorId, Boolean isPassed);

    List<PaymentGetRefundWaitingResponse> getRefundWaitingPayments();

    void updateRefundComplete(Long paymentId);

    List<PaymentGetSettlementOngoingResponse> getSettlementOngoingPayments();

    void updateSettlementComplete(Long paymentId);

    List<PostGetUnpaidPrivateResponse> getUnpaidPrivatePosts();

    void updatePostIsPaid(Long postId);

    List<CustomerGetByNicknameOrEmailResponse> getCustomersByNicknameOrEmail(String keyword);

    void updateCustomerIsBanned(Long customerId, Boolean isBanned);

    List<CounselorGetByNicknameOrEmailResponse> getCounselorsByNicknameOrEmail(String keyword);

    void updateCounselorPending(Long counselorId);

    PostGetByUuidResponse getPostByPostUuid(UUID postUuid);

    void deletePostByPostUuid(UUID postUuid);

    InformationGetResponse getInformation();

    Boolean updateShutdown(Boolean shutdown);

    Boolean getShutdown();
}
