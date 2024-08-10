package com.example.sharemind.counselor.application;

import static com.example.sharemind.global.constants.Constants.REALTIME_COUNSELOR;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.counselor.content.Bank;
import com.example.sharemind.counselor.content.ConsultStyle;
import com.example.sharemind.counselor.content.CounselorListSortType;
import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.domain.ConsultCost;
import com.example.sharemind.counselor.domain.ConsultTime;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.domain.ProfileRecord;
import com.example.sharemind.counselor.dto.request.CounselorGetRequest;
import com.example.sharemind.counselor.dto.request.CounselorUpdateAccountRequest;
import com.example.sharemind.counselor.dto.request.CounselorUpdateProfileRequest;
import com.example.sharemind.counselor.dto.response.*;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.counselor.repository.CounselorRepository;
import com.example.sharemind.counselor.repository.ProfileRecordRepository;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.searchWord.dto.request.SearchWordCounselorFindRequest;
import com.example.sharemind.wishList.application.WishListCounselorService;
import com.example.sharemind.wishList.domain.WishList;
import com.example.sharemind.wishList.dto.request.WishListGetRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselorServiceImpl implements CounselorService {

    private static final int COUNSELOR_PAGE = 4;
    private static final String SPLIT_HOURS = "~";

    private final CounselorRepository counselorRepository;
    private final ProfileRecordRepository profileRecordRepository;
    private final CustomerService customerService;
    private final WishListCounselorService wishListCounselorService;
    private final RedisTemplate<String, List<Long>> redisTemplate;

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

    @Override
    public List<Counselor> getAllCounselors() {
        return counselorRepository.findAllByIsEducatedIsTrueAndIsActivatedIsTrue();
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

            Counselor counselor = counselorRepository.save(
                    Counselor.builder()
                            .nickname(nickname)
                            .email(customer.getEmail())
                            .build()
            );
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
    public void updateCounselorProfile(CounselorUpdateProfileRequest counselorUpdateProfileRequest,
            Long customerId) {
        Counselor counselor = getCounselorByCustomerId(customerId);
        if ((counselor.getIsEducated() == null) || (!counselor.getIsEducated())) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_EDUCATED);
        } else if (ProfileStatus.EVALUATION_PENDING.equals(counselor.getProfileStatus())) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_ALREADY_IN_EVALUATION);
        }

        checkDuplicateNickname(counselorUpdateProfileRequest.getNickname(),
                counselor.getCounselorId());

        Set<ConsultCategory> consultCategories = new HashSet<>();
        for (String consultCategory : counselorUpdateProfileRequest.getConsultCategories()) {
            consultCategories.add(ConsultCategory.getConsultCategoryByName(consultCategory));
        }

        ConsultStyle consultStyle = ConsultStyle.getConsultStyleByName(
                counselorUpdateProfileRequest.getConsultStyle());

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
                        throw new CounselorException(CounselorErrorCode.COST_NOT_FOUND,
                                consultType.name());
                    }
                    consultCosts.add(
                            ConsultCost.builder()
                                    .consultType(consultType)
                                    .cost(chatCost)
                                    .build()
                    );
                }
                case LETTER -> {
                    if (letterCost == null) {
                        throw new CounselorException(CounselorErrorCode.COST_NOT_FOUND,
                                consultType.name());
                    }
                    consultCosts.add(
                            ConsultCost.builder()
                                    .consultType(consultType)
                                    .cost(letterCost)
                                    .build()
                    );
                }
            }
        }

        Set<ConsultTime> consultTimes = new HashSet<>();
        Map<String, List<String>> rawTimes = counselorUpdateProfileRequest.getConsultTimes();
        for (String day : rawTimes.keySet()) {
            List<String> times = rawTimes.get(day);
            consultTimes.add(ConsultTime.builder().day(day).times(times).build());
        }

        ProfileRecord profileRecord = ProfileRecord.builder()
                .counselor(counselor)
                .nickname(counselorUpdateProfileRequest.getNickname())
                .consultCosts(consultCosts)
                .consultTimes(consultTimes)
                .consultTypes(consultTypes)
                .consultCategories(consultCategories)
                .consultStyle(consultStyle)
                .experience(counselorUpdateProfileRequest.getExperience())
                .introduction(counselorUpdateProfileRequest.getIntroduction())
                .profileStatus(counselor.getProfileStatus())
                .build();
        profileRecordRepository.save(profileRecord);

        counselor.updateProfile(counselorUpdateProfileRequest.getNickname(), consultCategories,
                consultStyle, consultTypes, consultTimes, consultCosts,
                counselorUpdateProfileRequest.getIntroduction(),
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

    @Override
    public List<Counselor> getCounselorByWordWithPagination(
            SearchWordCounselorFindRequest searchWordCounselorFindRequest,
            String sortType) {
        String sortColumn = getCounselorSortColumn(sortType);
        Pageable pageable = PageRequest.of(searchWordCounselorFindRequest.getIndex(),
                COUNSELOR_PAGE,
                Sort.by(sortColumn).descending());
        Page<Counselor> page = counselorRepository.findByWordAndLevelAndStatus(
                searchWordCounselorFindRequest.getWord(),
                pageable);
        return page.getContent();
    }

    @Override
    public List<CounselorGetListResponse> getCounselorsByCategoryAndCustomer(Long customerId,
            String sortType,
            CounselorGetRequest counselorGetRequest) {
        List<Counselor> counselors = getCounselorByCategoryWithPagination(counselorGetRequest,
                sortType);
        List<Long> counselorIds = redisTemplate.opsForValue().get(REALTIME_COUNSELOR);
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Set<Long> wishListCounselorIds = wishListCounselorService.getWishListCounselorIdsByCustomer(
                customer);
        if (counselorIds == null) {
            return counselors.stream()
                    .map(counselor -> CounselorGetListResponse.of(counselor,
                            wishListCounselorIds.contains(counselor.getCounselorId()), false))
                    .toList();
        }
        return counselors.stream()
                .map(counselor -> CounselorGetListResponse.of(counselor,
                        wishListCounselorIds.contains(counselor.getCounselorId()),
                        counselorIds.contains(counselor.getCounselorId())))
                .toList();
    }

    @Override
    public List<CounselorGetRandomListResponse> getRandomCounselorsByCustomer(Long customerId,
            String sortType,
            int index) {

        String randomSortType = getCounselorSortType(sortType);
        List<Counselor> counselors = getCounselorWithPagination(index, randomSortType);
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Set<Long> wishListCounselorIds = wishListCounselorService.getWishListCounselorIdsByCustomer(
                customer);
        return counselors.stream()
                .map(counselor -> CounselorGetRandomListResponse.of(counselor,
                        wishListCounselorIds.contains(counselor.getCounselorId()), false,
                        randomSortType))
                .toList();
    }

    @Override
    public List<CounselorGetListResponse> getAllCounselorsByCategory(String sortType,
            CounselorGetRequest counselorGetRequest) {
        List<Counselor> counselors = getCounselorByCategoryWithPagination(counselorGetRequest,
                sortType);
        List<Long> counselorIds = redisTemplate.opsForValue().get(REALTIME_COUNSELOR);
        if (counselorIds == null) {
            return counselors.stream()
                    .map(counselor -> CounselorGetListResponse.of(counselor, false, false))
                    .toList();
        }
        return counselors.stream()
                .map(counselor -> CounselorGetListResponse.of(counselor, false,
                        counselorIds.contains(counselor.getCounselorId())))
                .toList();
    }

    @Override
    public List<CounselorGetRandomListResponse> getAllRandomCounselors(String sortType, int index) {
        String randomSortType = getCounselorSortType(sortType);
        List<Counselor> counselors = getCounselorWithPagination(index, randomSortType);

        return counselors.stream()
                .map(counselor -> CounselorGetRandomListResponse.of(counselor,
                        false, false, randomSortType))
                .toList();
    }

    public CounselorGetMinderProfileResponse getCounselorMinderProfileByCustomer(Long counselorId,
            Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = getCounselorByCounselorId(counselorId);

        return CounselorGetMinderProfileResponse.of(counselor,
                wishListCounselorService.getIsWishListByCustomerAndCounselor(customer, counselor));
    }

    @Override
    public CounselorGetMinderProfileResponse getAllCounselorMinderProfile(Long counselorId) {
        Counselor counselor = getCounselorByCounselorId(counselorId);

        return CounselorGetMinderProfileResponse.of(counselor, false);
    }

    @Transactional
    @Override
    public void updateAccount(CounselorUpdateAccountRequest counselorUpdateAccountRequest,
            Long customerId) {
        Counselor counselor = getCounselorByCustomerId(customerId);
        Bank.existsByDisplayName(counselorUpdateAccountRequest.getBank());
        counselor.updateAccountInfo(counselorUpdateAccountRequest.getAccount(),
                counselorUpdateAccountRequest.getBank(),
                counselorUpdateAccountRequest.getAccountHolder());
    }

    @Override
    public CounselorGetAccountResponse getAccount(Long customerId) {
        Counselor counselor = getCounselorByCustomerId(customerId);

        return CounselorGetAccountResponse.of(counselor);
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
    public CounselorGetForConsultResponse getCounselorForConsultCreation(Long counselorId,
            String type) {
        Counselor counselor = getCounselorByCounselorId(counselorId);
        ConsultType consultType = ConsultType.getConsultTypeByName(type);
        if (!counselor.getConsultTypes().contains(consultType)) {
            throw new CounselorException(CounselorErrorCode.INVALID_CONSULT_TYPE);
        }

        return CounselorGetForConsultResponse.of(counselor, consultType);
    }

    @Override
    public CounselorGetBannerResponse getCounselorChatBanner(Chat chat) {
        Counselor counselor = getCounselorByCounselorId(
                chat.getConsult().getCounselor().getCounselorId());
        return CounselorGetBannerResponse.of(counselor);
    }

    @Override
    public List<CounselorGetWishListResponse> getCounselorWishListByCustomer(
            WishListGetRequest wishListGetRequest, Long customerId) {
        List<WishList> wishLists = wishListCounselorService.getWishList(wishListGetRequest,
                customerId);
        List<Long> counselorIds = redisTemplate.opsForValue().get(REALTIME_COUNSELOR);

        return wishLists.stream()
                .map(wishList -> CounselorGetWishListResponse.of(wishList,
                        counselorIds != null && counselorIds.contains(
                                wishList.getCounselor().getCounselorId())))
                .toList();
    }

    @Override
    public void checkCounselorAndCustomerSame(Customer customer, Counselor counselor) {
        if (customer.getCounselor() != null && customer.getCounselor() == counselor) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_AND_CUSTOMER_SAME,
                    customer.getCustomerId().toString());
        }
    }

    @Override
    public List<Counselor> getCounselorsByNicknameOrEmail(String keyword) {
        return counselorRepository.findAllByNicknameOrEmail(keyword);
    }

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void updateRealtimeCounselors() {
        List<Counselor> counselors = counselorRepository.findAllByProfileStatusIsEvaluationCompleteAndIsActivatedIsTrue();
        String currentDay = LocalDate.now().getDayOfWeek().toString().substring(0, 3).toUpperCase();
        int currentHour = LocalTime.now().getHour();

        List<Counselor> realtimeCounselors = counselors.stream()
                .filter(counselor -> isAvailableAtRealTime(counselor.getConsultTimes(), currentDay,
                        currentHour))
                .sorted((c1, c2) -> Long.compare(c2.getTotalConsult(), c1.getTotalConsult()))
                .toList();

        List<Long> counselorIds = realtimeCounselors.stream()
                .map(Counselor::getCounselorId)
                .toList();

        redisTemplate.opsForValue().set(REALTIME_COUNSELOR, counselorIds);
    }

    private List<Counselor> getCounselorByCategoryWithPagination(
            CounselorGetRequest counselorGetRequest, String sortType) {
        String sortColumn = getCounselorSortColumn(sortType);
        Pageable pageable = PageRequest.of(counselorGetRequest.getIndex(), COUNSELOR_PAGE,
                Sort.by(sortColumn).descending());
        if (counselorGetRequest.getConsultCategory() == null) {
            return getRealtimeCounselors(counselorGetRequest.getIndex());
        }

        ConsultCategory consultCategory = ConsultCategory.getConsultCategoryByName(
                counselorGetRequest.getConsultCategory());
        return counselorRepository.findByConsultCategoryAndLevelAndStatus(consultCategory, pageable)
                .getContent();
    }

    private List<Counselor> getCounselorWithPagination(int index, String sortType) {
        String sortColumn = getCounselorSortColumn(sortType);
        Pageable pageable = PageRequest.of(index, COUNSELOR_PAGE,
                Sort.by(sortColumn).descending());
        return counselorRepository.findByLevelAndStatus(pageable).getContent();
    }

    private String getCounselorSortColumn(String sortType) {
        CounselorListSortType counselorListSortType = CounselorListSortType.getSortTypeByName(
                sortType);
        return counselorListSortType.getSortColumn();
    }

    private List<Counselor> getRealtimeCounselors(int index) {
        int start = index * COUNSELOR_PAGE;
        List<Long> counselorIds = redisTemplate.opsForValue().get(REALTIME_COUNSELOR);
        if (counselorIds == null || start >= counselorIds.size()) {
            return Collections.emptyList();
        }

        List<Long> counselorsSubList = (counselorIds.size() >= start + COUNSELOR_PAGE) ?
                counselorIds.subList(start, start + COUNSELOR_PAGE)
                : counselorIds.subList(start, counselorIds.size());
        return counselorRepository.findAllById(counselorsSubList);
    }

    private void checkDuplicateNickname(String nickname, Long counselorId) {
        if (counselorRepository.existsByNicknameAndCounselorIdNot(nickname, counselorId)) {
            throw new CounselorException(CounselorErrorCode.DUPLICATE_NICKNAME);
        }
    }

    private boolean isAvailableAtRealTime(Set<ConsultTime> consultTimes, String day, int hour) {
        for (ConsultTime consultTime : consultTimes) {
            if (Objects.equals(consultTime.getDay().toString(), day)) {
                for (String timeRange : consultTime.getTimes()) {
                    String[] parts = timeRange.split(SPLIT_HOURS);
                    int startHour = Integer.parseInt(parts[0]);
                    int endHour = Integer.parseInt(parts[1]);
                    if (hour >= startHour && hour < endHour) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String getCounselorSortType(String sortType) {
        if ("RANDOM".equals(sortType)) {
            return CounselorListSortType.getRandomSortType();
        }
        return sortType;
    }
}
