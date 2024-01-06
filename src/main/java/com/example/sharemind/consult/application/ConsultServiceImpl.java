package com.example.sharemind.consult.application;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.dto.request.ConsultCreateRequest;
import com.example.sharemind.consult.dto.response.ConsultCreateResponse;
import com.example.sharemind.consult.repository.ConsultRepository;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.counselor.repository.CounselorRepository;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConsultServiceImpl implements ConsultService {

    private final ConsultRepository consultRepository;
    private final CounselorRepository counselorRepository;

    @Transactional
    @Override
    public ConsultCreateResponse createConsult(ConsultCreateRequest consultCreateRequest, Customer customer) {

        // TODO 프로필 수정 심사 중인지도 확인해야할 것 같음
        Counselor counselor = counselorRepository.findByCounselorIdAndIsActivatedIsTrue(consultCreateRequest.getCounselorId())
                .orElseThrow(() -> new CounselorException(CounselorErrorCode.COUNSELOR_NOT_FOUND, consultCreateRequest.getCounselorId().toString()));

        ConsultType consultType = ConsultType.getConsultTypeByName(consultCreateRequest.getConsultTypeName());
        Long cost = counselor.getConsultCost(consultType);

        Consult consult = Consult.builder()
                .customer(customer)
                .counselor(counselor)
                .consultType(consultType)
                .cost(cost)
                .build();
        consultRepository.save(consult);

        return ConsultCreateResponse.of(consult, counselor);
    }
}
