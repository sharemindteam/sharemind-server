package com.example.sharemind.consult.application;

import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.dto.request.ConsultCreateRequest;
import com.example.sharemind.consult.dto.response.ConsultCreateResponse;
import com.example.sharemind.customer.domain.Customer;

import java.util.List;

public interface ConsultService {
    ConsultCreateResponse createConsult(ConsultCreateRequest consultCreateRequest, Customer customer);

    Consult getConsultByConsultId(Long consultId);

    List<Consult> getUnpaidConsults();
}
