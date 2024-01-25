package com.example.sharemind.counselor.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.counselor.content.ConsultStyle;
import com.example.sharemind.counselor.content.CounselorListSortType;
import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.domain.ConsultCost;
import com.example.sharemind.counselor.domain.ConsultTime;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.request.CounselorGetRequest;
import com.example.sharemind.counselor.dto.request.CounselorUpdateProfileRequest;
import com.example.sharemind.counselor.dto.response.*;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.counselor.repository.CounselorRepository;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.searchWord.dto.request.SearchWordFindRequest;
import com.example.sharemind.wishlist.application.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselorServiceImpl implements CounselorService {

    private static final int COUNSELOR_PAGE = 4;

    private final CounselorRepository counselorRepository;
    private final CustomerService customerService;
    private final WishListService wishListService;

    @Override
    public Counselor getCounselorByCounselorId(Long counselorId) {
        return counselorRepository.findByCounselorIdAndIsActivatedIsTrue(counselorId)
                .orElseThrow(() -> new CounselorException(CounselorErrorCode.COUNSELOR_NOT_FOUND,
                        counselorId.toString()));
    }

    @Override
    public Counselor getCounselorByCustomerId(Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = customer.getCounselor();
        if (counselor == null) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_FOUND);
        }
        return counselor;
    }

    @Transactional
    @Override
    public void updateIsEducated(Boolean isEducated, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        if (customer.getCounselor() == null) {
            String nickname = "마인더" + new Random().nextInt(99999);
            while (counselorRepository.existsByNickname(nickname)) {
                nickname = "마인더" + new Random().nextInt(99999);
            }

            Counselor counselor = counselorRepository.save(Counselor.builder().nickname(nickname).build());
            customer.setCounselor(counselor);
        }

        Counselor counselor = customer.getCounselor();
        counselor.updateIsEducated(isEducated);
    }

    @Override
    public Boolean getRetryPermission(Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = customer.getCounselor();

        if ((counselor == null) || (counselor.getRetryEducation() == null)) {
            return true;
        } else if (counselor.getIsEducated()) {
            return false;
        }
        return counselor.getRetryEducation().isBefore(LocalDateTime.now());
    }

    @Transactional
    @Override
    public void updateCounselorProfile(CounselorUpdateProfileRequest counselorUpdateProfileRequest, Long customerId) {
        Counselor counselor = getCounselorByCustomerId(customerId);
        if ((counselor.getIsEducated() == null) || (!counselor.getIsEducated())) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_EDUCATED);
        } else if (ProfileStatus.EVALUATION_PENDING.equals(counselor.getProfileStatus())) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_ALREADY_IN_EVALUATION);
        }

        checkDuplicateNickname(counselorUpdateProfileRequest.getNickname(), counselor.getCounselorId());

        Set<ConsultCategory> consultCategories = new HashSet<>();
        for (String consultCategory : counselorUpdateProfileRequest.getConsultCategories()) {
            consultCategories.add(ConsultCategory.getConsultCategoryByName(consultCategory));
        }

        ConsultStyle consultStyle = ConsultStyle.getConsultStyleByName(counselorUpdateProfileRequest.getConsultStyle());

        Set<ConsultType> consultTypes = new HashSet<>();
        for (String consultType : counselorUpdateProfileRequest.getConsultTypes()) {
            ConsultType type = ConsultType.getConsultTypeByName(consultType);
            consultTypes.add(type);
        }

        Set<ConsultCost> consultCosts = new HashSet<>();
        Long letterCost = counselorUpdateProfileRequest.getLetterCost();
        Long chatCost = counselorUpdateProfileRequest.getChatCost();
        for (ConsultType consultType : consultTypes) {
            switch (consultType) {
                case CHAT -> {
                    if (chatCost == null) {
                        throw new CounselorException(CounselorErrorCode.COST_NOT_FOUND, consultType.name());
                    }
                    consultCosts.add(ConsultCost.builder().consultType(consultType).cost(chatCost).build());
                }
                case LETTER -> {
                    if (letterCost == null) {
                        throw new CounselorException(CounselorErrorCode.COST_NOT_FOUND, consultType.name());
                    }
                    consultCosts.add(ConsultCost.builder().consultType(consultType).cost(letterCost).build());
                }
            }
        }

        Set<ConsultTime> consultTimes = new HashSet<>();
        Map<String, List<String>> rawTimes = counselorUpdateProfileRequest.getConsultTimes();
        for (String day : rawTimes.keySet()) {
            List<String> times = rawTimes.get(day);
            consultTimes.add(ConsultTime.builder().day(day).times(times).build());
        }

        counselor.updateProfile(counselorUpdateProfileRequest.getNickname(), consultCategories, consultStyle,
                consultTypes, consultTimes, consultCosts, counselorUpdateProfileRequest.getIntroduction(),
                counselorUpdateProfileRequest.getExperience());
    }

    @Override
    public CounselorGetProfileResponse getCounselorProfile(Long customerId) {
        Counselor counselor = getCounselorByCustomerId(customerId);
        if ((counselor.getIsEducated() == null) || (counselor.getIsEducated().equals(false))) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_EDUCATED);
        }

        return CounselorGetProfileResponse.of(counselor);
    }

    @Override
    public List<Counselor> getEvaluationPendingConsults() {
        return counselorRepository.findAllByProfileStatusIsEvaluationPendingAndIsActivatedIsTrue();
    }

    private List<Counselor> getCounselorByCategoryWithPagination(CounselorGetRequest counselorGetRequest,
                                                                 String sortType) {
        String sortColumn = getCounselorSortColumn(sortType);
        Pageable pageable = PageRequest.of(counselorGetRequest.getIndex(), COUNSELOR_PAGE,
                Sort.by(sortColumn).descending());
        if (counselorGetRequest.getConsultCategory() == null) {
            return counselorRepository.findByLevelAndStatus(pageable).getContent();
        }

        ConsultCategory consultCategory = ConsultCategory.getConsultCategoryByName(
                counselorGetRequest.getConsultCategory());
        return counselorRepository.findByConsultCategoryAndLevelAndStatus(consultCategory, pageable).getContent();
    }

    @Override
    public List<Counselor> getCounselorByWordWithPagination(SearchWordFindRequest searchWordFindRequest,
                                                            String sortType) {
        String sortColumn = getCounselorSortColumn(sortType);
        Pageable pageable = PageRequest.of(searchWordFindRequest.getIndex(), COUNSELOR_PAGE,
                Sort.by(sortColumn).descending());
        Page<Counselor> page = counselorRepository.findByWordAndLevelAndStatus(searchWordFindRequest.getWord(),
                pageable);
        return page.getContent();
    }

    @Override
    public List<CounselorGetListResponse> getCounselorsByCategory(Long customerId, String sortType,
                                                                  CounselorGetRequest counselorGetRequest) {
        List<Counselor> counselors = getCounselorByCategoryWithPagination(counselorGetRequest, sortType);

        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Set<Long> wishListCounselorIds = wishListService.getWishListCounselorIdsByCustomer(customer);

        return counselors.stream()
                .map(counselor -> CounselorGetListResponse.of(counselor,
                        wishListCounselorIds.contains(counselor.getCounselorId())))
                .toList();
    }

    @Override
    public CounselorGetMinderProfileResponse getCounselorMinderProfile(Long counselorId) {
        Counselor counselor = getCounselorByCounselorId(counselorId);

        return CounselorGetMinderProfileResponse.of(counselor);
    }

    private String getCounselorSortColumn(String sortType) {
        CounselorListSortType counselorListSortType = CounselorListSortType.getSortTypeByName(sortType);
        return counselorListSortType.getSortColumn();
    }

    @Override
    public CounselorGetInfoResponse getCounselorMyInfo(Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = customer.getCounselor();
        if (counselor == null) {
            return CounselorGetInfoResponse.of();
        }

        return CounselorGetInfoResponse.of(counselor);
    }

    @Override
    public CounselorGetForConsultResponse getCounselorForConsultCreation(Long counselorId, String type) {
        Counselor counselor = getCounselorByCounselorId(counselorId);
        ConsultType consultType = ConsultType.getConsultTypeByName(type);
        if (!counselor.getConsultTypes().contains(consultType)) {
            throw new CounselorException(CounselorErrorCode.INVALID_CONSULT_TYPE);
        }

        return CounselorGetForConsultResponse.of(counselor, consultType);
    }

    private void checkDuplicateNickname(String nickname, Long counselorId) {
        if (counselorRepository.existsByNicknameAndCounselorIdNot(nickname, counselorId)) {
            throw new CounselorException(CounselorErrorCode.DUPLICATE_NICKNAME);
        }
    }

    @Override
    public CounselorGetBannerResponse getCounselorChatBanner(Chat chat) {
        Counselor counselor = getCounselorByCounselorId(chat.getConsult().getCounselor().getCounselorId());
        return CounselorGetBannerResponse.of(counselor);
    }
}
