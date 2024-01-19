package com.example.sharemind.counselor.application;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.counselor.repository.CounselorRepository;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_FOUND, null);
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
        }

        Counselor counselor = customer.getCounselor();
        counselor.updateIsEducated(isEducated);
    }

    @Override
    public Boolean getRetryPermission(Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = customer.getCounselor();
        if (counselor == null) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_FOUND, null);
        }

        if (counselor.getRetryEducation() == null) {
            return true;
        } else if (counselor.getIsEducated()) {
            return false;
        }
        return counselor.getRetryEducation().isBefore(LocalDateTime.now());
    }
}
