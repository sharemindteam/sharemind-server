package com.example.sharemind.counselor.application;

import com.example.sharemind.counselor.content.ConsultStyle;
import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.domain.ConsultCost;
import com.example.sharemind.counselor.domain.ConsultTime;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.request.CounselorUpdateProfileRequest;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.counselor.repository.CounselorRepository;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultCategory;
import com.example.sharemind.global.content.ConsultType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselorServiceImpl implements CounselorService {

    private final CounselorRepository counselorRepository;
    private final CustomerService customerService;

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
            Counselor counselor = counselorRepository.save(Counselor.builder().isEducated(isEducated).build());
            customer.setCounselor(counselor);
            return;
        }

        Counselor counselor = customer.getCounselor();
        counselor.updateIsEducated(isEducated);
    }

    @Override
    public Boolean getRetryPermission(Long customerId) {
        Counselor counselor = getCounselorByCustomerId(customerId);

        if (counselor.getRetryEducation() == null) {
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
    public List<Counselor> getEvaluationPendingConsults() {
        return counselorRepository.findAllByProfileStatusIsEvaluationPendingAndIsActivatedIsTrue();
    }
}
