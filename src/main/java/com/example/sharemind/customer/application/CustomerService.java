package com.example.sharemind.customer.application;

import com.example.sharemind.customer.domain.Customer;

public interface CustomerService {
    Customer getCustomerByCustomerId(Long customerId);

    void checkDuplicateEmail(String email);
}
