package com.example.sharemind.consult.application;

import com.example.sharemind.consult.dto.request.ConsultCreateRequest;
import com.example.sharemind.consult.dto.response.ConsultCreateResponse;
import com.example.sharemind.customer.domain.Customer;

public interface ConsultService {
    ConsultCreateResponse createConsult(ConsultCreateRequest consultCreateRequest, Customer customer);
}
