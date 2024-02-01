package com.example.sharemind.payment.application;

import com.example.sharemind.consult.content.ConsultStatus;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.payment.content.PaymentCounselorStatus;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.payment.dto.response.PaymentGetCustomerResponse;
import com.example.sharemind.payment.exception.PaymentErrorCode;
import com.example.sharemind.payment.exception.PaymentException;
import com.example.sharemind.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int PAYMENT_CUSTOMER_PAGE_SIZE = 4;
    private static final int PAYMENT_FIXED_OFFSET = 7;

    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;

    @Override
    public Payment getPaymentByPaymentId(Long paymentId) {
        return paymentRepository.findByPaymentIdAndIsActivatedIsTrue(paymentId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND, paymentId.toString()));
    }

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

    @Transactional
    @Override
    public void updateRefundWaitingByCustomer(Long paymentId, Long customerId) {
        Payment payment = getPaymentByPaymentId(paymentId);
        payment.checkUpdateAuthority(customerId);
        if ((payment.getCustomerStatus() == null) ||
                (!payment.getCustomerStatus().equals(PaymentCustomerStatus.PAYMENT_COMPLETE)) ||
                (!payment.getConsult().getConsultStatus().equals(ConsultStatus.WAITING))) {
            throw new PaymentException(PaymentErrorCode.INVALID_REFUND_WAITING, paymentId.toString());
        }

        payment.updateCustomerStatusRefundWaiting();
    }

    @Override
    public List<Payment> getRefundWaitingPayments() {
        return paymentRepository.findAllByCustomerStatusAndIsActivatedIsTrue(PaymentCustomerStatus.REFUND_WAITING);
    }

    @Override
    public Boolean checkRefundWaitingExists(Customer customer) {
        return paymentRepository.existsByConsultCustomerAndCustomerStatusAndIsActivatedIsTrue(customer,
                PaymentCustomerStatus.REFUND_WAITING);
    }

    @Override
    public Boolean checkNotSettlementCompleteAndNotNoneExists(Counselor counselor) {
        return paymentRepository.findTopByConsultCounselorAndCounselorStatusIsNotNoneAndFinishAndIsActivatedIsTrue(
                counselor) != null;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkPaymentConsultFinishToSettlementWaiting() {
        paymentRepository.findAllByCounselorStatusAndIsActivatedIsTrue(PaymentCounselorStatus.CONSULT_FINISH).stream()
                .filter(payment -> payment.getUpdatedAt().plusDays(PAYMENT_FIXED_OFFSET).isBefore(LocalDateTime.now()))
                .forEach(Payment::updateCounselorStatusSettlementWaiting);
    }
}
