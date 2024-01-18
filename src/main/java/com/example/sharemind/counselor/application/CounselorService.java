package com.example.sharemind.counselor.application;

import com.example.sharemind.counselor.domain.Counselor;

public interface CounselorService {
    Counselor getCounselorByCounselorId(Long counselorId);

    Counselor getCounselorByCustomerId(Long customerId);

    void updateIsEducated(Boolean isEducated, Long customerId);
}
