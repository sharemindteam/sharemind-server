package com.example.sharemind.counselor.application;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.request.CounselorUpdateProfileRequest;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;
import java.util.List;

public interface CounselorService {
    Counselor getCounselorByCounselorId(Long counselorId);

    Counselor getCounselorByCustomerId(Long customerId);

    void updateIsEducated(Boolean isEducated, Long customerId);

    Boolean getRetryPermission(Long customerId);

    void updateCounselorProfile(CounselorUpdateProfileRequest counselorUpdateProfileRequest, Long customerId);

    List<Counselor> getEvaluationPendingConsults();

    List<Counselor> getCounselorByWordWithPagination(SearchWordFindRequest searchWordFindRequest);
}
