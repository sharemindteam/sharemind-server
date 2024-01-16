package com.example.sharemind.customer.application;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.customer.exception.CustomerErrorCode;
import com.example.sharemind.customer.exception.CustomerException;
import com.example.sharemind.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer getCustomerByCustomerId(Long customerId) {
        return customerRepository.findByCustomerIdAndIsActivatedIsTrue(customerId)
                .orElseThrow(() -> new CustomerException(
                        CustomerErrorCode.CUSTOMER_NOT_FOUND, customerId.toString()));
    }

    @Override
    public void checkDuplicateEmail(String email) {
        if (customerRepository.existsByEmailAndIsActivatedIsTrue(email)) {
            throw new CustomerException(CustomerErrorCode.EMAIL_ALREADY_EXIST, email);
        }
    }
}
