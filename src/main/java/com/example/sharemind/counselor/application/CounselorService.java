package com.example.sharemind.counselor.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.request.CounselorGetRequest;
import com.example.sharemind.counselor.dto.request.CounselorUpdateAccountRequest;
import com.example.sharemind.counselor.dto.request.CounselorUpdateProfileRequest;
import com.example.sharemind.counselor.dto.response.*;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;

import com.example.sharemind.wishList.dto.request.WishListGetRequest;

import java.util.List;

public interface CounselorService {

    Counselor getCounselorByCounselorId(Long counselorId);

    Counselor getCounselorByCustomerId(Long customerId);

    List<Counselor> getAllCounselors();

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

    List<CounselorGetListResponse> getCounselorsByCategoryAndCustomer(Long customerId, String sortType,
                                                                      CounselorGetRequest counselorGetRequest);

    List<CounselorGetListResponse> getAllCounselorsByCategory(String sortType,
                                                              CounselorGetRequest counselorGetRequest);

    List<CounselorGetWishListResponse> getCounselorWishListByCustomer(WishListGetRequest wishListGetRequest, Long customerId);

    CounselorGetMinderProfileResponse getCounselorMinderProfileByCustomer(Long counselorId, Long customerId);

    CounselorGetMinderProfileResponse getAllCounselorMinderProfile(Long counselorId);

    void updateAccount(CounselorUpdateAccountRequest counselorUpdateAccountRequest, Long customerId);

    void checkCounselorAndCustomerSame(Customer customer, Counselor counselor);
}
