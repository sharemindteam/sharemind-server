package com.example.sharemind.counselor.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.request.CounselorGetRequest;
import com.example.sharemind.counselor.dto.request.CounselorUpdateProfileRequest;
import com.example.sharemind.counselor.dto.response.*;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;

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

    CounselorGetForConsultResponse getCounselorForConsultCreation(Long counselorId, String consultType);

    CounselorGetBannerResponse getCounselorChatBanner(Chat chat);

    List<CounselorGetListResponse> getCounselorsByCategory(Long customerId, String sortType,
                                                           CounselorGetRequest counselorGetRequest);

    List<CounselorGetWishListResponse> getCounselorWishListByCustomer(Long wishlistId, Long customerId);

    CounselorGetMinderProfileResponse getCounselorMinderProfile(Long counselorId);
}
