package com.example.sharemind.counselor.application;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.request.CounselorUpdateProfileRequest;
import com.example.sharemind.counselor.dto.response.CounselorGetBannerResponse;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;
import com.example.sharemind.counselor.dto.response.CounselorGetInfoResponse;
import com.example.sharemind.counselor.dto.response.CounselorGetProfileResponse;

import java.util.List;

public interface CounselorService {

    Counselor getCounselorByCounselorId(Long counselorId);

    Counselor getCounselorByCustomerId(Long customerId);

    void updateIsEducated(Boolean isEducated, Long customerId);

    Boolean getRetryPermission(Long customerId);

    void updateCounselorProfile(CounselorUpdateProfileRequest counselorUpdateProfileRequest, Long customerId);

    CounselorGetProfileResponse getCounselorProfile(Long customerId);

    List<Counselor> getEvaluationPendingConsults();

    List<Counselor> getCounselorByWordWithPagination(SearchWordFindRequest searchWordFindRequest,
                                                     String sortType);

    CounselorGetInfoResponse getCounselorMyInfo(Long customerId);

    CounselorGetBannerResponse getCounselorChatBanner(Long chatId, Boolean isCustomer, Long customerId);
}
