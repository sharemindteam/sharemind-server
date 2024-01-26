package com.example.sharemind.payment.application;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import com.example.sharemind.payment.dto.response.PaymentGetCustomerResponse;
import com.example.sharemind.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int PAYMENT_CUSTOMER_PAGE_SIZE = 4;

    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;

    @Override
    public List<PaymentGetCustomerResponse> getPaymentsByCustomer(Long paymentId, String status, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        PaymentCustomerStatus customerStatus = PaymentCustomerStatus.getPaymentCustomerStatusByName(status);

        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, PAYMENT_CUSTOMER_PAGE_SIZE);
        Page<PaymentGetCustomerResponse> page =
                (paymentId == 0 ?
                        paymentRepository.findAllByCustomerAndCustomerStatusAndIsActivatedIsTrue(
                                customer, customerStatus, pageable) :
                        paymentRepository.findAllByPaymentIdLessThanAndCustomerAndCustomerStatusAndIsActivatedIsTrue(
                                paymentId, customer, customerStatus, pageable))
                        .map(PaymentGetCustomerResponse::of);

        return page.getContent();
    }
}
