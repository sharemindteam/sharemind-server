package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultsGetUnpaidResponse;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.exception.ConsultErrorCode;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.consult.repository.ConsultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ConsultRepository consultRepository;

    @Override
    public List<ConsultsGetUnpaidResponse> getUnpaidConsults() {
        return consultRepository.findAllByIsPaidIsFalseAndIsActivatedIsTrue().stream()
                .map(ConsultsGetUnpaidResponse::of)
                .toList();
    }

    @Transactional
    public void updateIsPaid(Long consultId) {

        Consult consult = consultRepository.findByConsultIdAndIsActivatedIsTrue(consultId)
                .orElseThrow(() -> new ConsultException(ConsultErrorCode.CONSULT_NOT_FOUND, consultId.toString()));
        consult.updateIsPaid();

        // TODO 상담 유형에 따라 비실시간/실시간 상담 생성
        /**
         * switch (consult.getConsultType()) {
         *             case REALTIME ->
         *             case NON_REALTIME ->
         *         }
         */
    }
}
