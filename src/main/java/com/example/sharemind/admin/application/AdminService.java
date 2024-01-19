package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultsGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.CounselorGetPendingResponse;

import java.util.List;

public interface AdminService {
    List<ConsultsGetUnpaidResponse> getUnpaidConsults();

    void updateIsPaid(Long consultId);

    List<CounselorGetPendingResponse> getPendingCounselors();
}
