package com.example.sharemind.customer.application;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import java.util.List;

public interface CustomerService {
    Customer getCustomerByCustomerId(Long customerId);

    Customer getCustomerByCounselor(Counselor counselor);

    String getCustomerNickname(Long customerId);

    List<Customer> getCustomersByNicknameOrEmail(String keyword);
}
