package com.example.sharemind.consult.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.dto.request.ConsultCreateRequest;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ConsultType;

import java.util.List;

public interface ConsultService {
    void createConsult(ConsultCreateRequest consultCreateRequest, Long customerId);

    Consult getConsultByConsultId(Long consultId);

    List<Consult> getUnpaidConsults();

    List<Consult> getConsultsByCustomerIdAndConsultTypeAndIsPaid(Long customerId, ConsultType consultType);

    List<Consult> getConsultsByCounselorIdAndConsultTypeAndIsPaid(Long counselorId, ConsultType consultType);

    Consult getConsultByChat(Chat chat);

    Boolean checkWaitingOrOngoingExistsByCustomer(Customer customer);

    Boolean checkWaitingOrOngoingExistsByCounselor(Counselor counselor);
}
