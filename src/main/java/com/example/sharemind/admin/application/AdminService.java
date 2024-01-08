package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultsGetUnpaidResponse;

import java.util.List;

public interface AdminService {
    List<ConsultsGetUnpaidResponse> getUnpaidConsults();
    void updateIsPaid(Long consultId);
}
