package com.example.sharemind.consult.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.dto.request.ConsultCreateRequest;
import com.example.sharemind.consult.exception.ConsultErrorCode;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.consult.repository.ConsultRepository;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConsultServiceImpl implements ConsultService {

    private final ConsultRepository consultRepository;
    private final CounselorService counselorService;
    private final CustomerService customerService;

    @Transactional
    @Override
    public void createConsult(ConsultCreateRequest consultCreateRequest, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        Counselor counselor = counselorService.getCounselorByCounselorId(consultCreateRequest.getCounselorId());
        if (!counselor.getProfileStatus().equals(ProfileStatus.EVALUATION_COMPLETE)) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_COMPLETE_EVALUATION);
        }

        ConsultType consultType = ConsultType.getConsultTypeByName(consultCreateRequest.getConsultTypeName());
        if (!counselor.getConsultTypes().contains(consultType)) {
            throw new CounselorException(CounselorErrorCode.INVALID_CONSULT_TYPE);
        }
        Long cost = counselor.getConsultCost(consultType);

        Consult consult = Consult.builder()
                .customer(customer)
                .counselor(counselor)
                .consultType(consultType)
                .cost(cost)
                .build();
        consultRepository.save(consult);
    }

    @Override
    public Consult getConsultByConsultId(Long consultId) {
        return consultRepository.findByConsultIdAndIsActivatedIsTrue(consultId)
                .orElseThrow(() -> new ConsultException(ConsultErrorCode.CONSULT_NOT_FOUND,
                        consultId.toString()));
    }

    @Override
    public List<Consult> getUnpaidConsults() {
        return consultRepository.findAllByIsPaidIsFalseAndIsActivatedIsTrue();
    }

    @Override
    public List<Consult> getConsultsByCustomerIdAndConsultTypeAndIsPaid(Long customerId, ConsultType consultType) {
        return consultRepository.findByCustomerIdAndConsultTypeAndIsPaid(customerId, consultType);
    }

    @Override
    public List<Consult> getConsultsByCounselorIdAndConsultTypeAndIsPaid(Long counselorId, ConsultType consultType) {
        return consultRepository.findByCounselorIdAndConsultTypeAndIsPaid(counselorId, consultType);
    }

    @Override
    public Consult getConsultByChat(Chat chat) {
        return consultRepository.findByChatAndIsActivatedIsTrue(chat).orElseThrow(
                () -> new ConsultException(ConsultErrorCode.CONSULT_NOT_FOUND, "chatId : " + chat.getChatId()));
    }

    @Override
    public Boolean checkWaitingOrOngoingExistsByCustomer(Customer customer) {
        return consultRepository.findTopByConsultStatusIsWaitingOrOngoingAndCustomerAndIsPaid(customer) != null;
    }

    @Override
    public Boolean checkWaitingOrOngoingExistsByCounselor(Counselor counselor) {
        return consultRepository.findTopByConsultStatusIsWaitingOrOngoingAndCounselorAndIsPaid(counselor) != null;
    }
}
