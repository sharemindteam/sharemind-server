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
        return customer.getCounselor();
    }
}
