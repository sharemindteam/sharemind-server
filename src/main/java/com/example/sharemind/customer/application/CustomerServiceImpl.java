package com.example.sharemind.customer.application;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.customer.exception.CustomerErrorCode;
import com.example.sharemind.customer.exception.CustomerException;
import com.example.sharemind.customer.repository.CustomerRepository;
import java.util.List;
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
    public Customer getCustomerByCounselor(Counselor counselor) {
        return customerRepository.findByCounselorAndIsActivatedIsTrue(counselor)
                .orElseThrow(() -> new CustomerException(CustomerErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Override
    public String getCustomerNickname(Long customerId) {
        return getCustomerByCustomerId(customerId).getNickname();
    }

    @Override
    public List<Customer> getCustomersByNicknameOrEmail(String keyword) {
        return customerRepository.findAllByNicknameOrEmail(keyword);
    }
}
