package com.example.sharemind.payment.application;

import com.example.sharemind.consult.content.ConsultStatus;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.domain.Settlement;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.payment.content.PaymentCounselorStatus;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import com.example.sharemind.payment.content.PaymentSortType;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.payment.dto.response.PaymentGetCounselorResponse;
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

import static com.example.sharemind.global.constants.Constants.FEE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int PAYMENT_CUSTOMER_PAGE_SIZE = 4;
    private static final int PAYMENT_COUNSELOR_PAGE_SIZE = 3;
    private static final int PAYMENT_FIXED_OFFSET = 7;

    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;
    private final CounselorService counselorService;

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
        payment.checkUpdateAuthorityByCustomer(customerId);
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
    public List<PaymentGetCounselorResponse> getPaymentsByCounselor(Long paymentId, String status, String sort, Long customerId) {
        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);
        PaymentCounselorStatus counselorStatus = PaymentCounselorStatus.getPaymentCounselorStatusByName(status);
        PaymentSortType sortType = PaymentSortType.getPaymentSortTypeByName(sort);

        Long total = null;
        Settlement settlement = counselor.getSettlement();
        switch (counselorStatus) { // TODO 최적화 필요...
            case SETTLEMENT_WAITING -> {
                switch (sortType) {
                    case WEEK -> total = settlement.getWaitingWeek();
                    case MONTH -> total = settlement.getWaitingMonth();
                    case ALL -> total = settlement.getWaitingAll();
                }
            }
            case SETTLEMENT_ONGOING -> {
                switch (sortType) {
                    case WEEK -> total = settlement.getOngoingWeek();
                    case MONTH -> total = settlement.getOngoingMonth();
                    case ALL -> total = settlement.getOngoingAll();
                }
            }
            case SETTLEMENT_COMPLETE -> {
                switch (sortType) {
                    case WEEK -> total = settlement.getCompleteWeek();
                    case MONTH -> total = settlement.getCompleteMonth();
                    case ALL -> total = settlement.getCompleteAll();
                }
            }
        }

        LocalDateTime sortTime = null;
        switch (sortType) {
            case WEEK -> sortTime = LocalDateTime.now().minusWeeks(1);
            case MONTH -> sortTime = LocalDateTime.now().minusMonths(1);
            case ALL -> sortTime = LocalDateTime.MIN;
        }

        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, PAYMENT_COUNSELOR_PAGE_SIZE);
        Long finalTotal = total;
        Page<PaymentGetCounselorResponse> page =
                (paymentId == 0 ?
                        paymentRepository.findAllByCounselorAndCounselorStatusAndUpdatedAtIsBefore(
                                counselor, counselorStatus, sortTime, pageable) :
                        paymentRepository.findAllByPaymentIdLessThanAndCounselorAndCounselorStatusAndUpdatedAtIsBefore(
                                paymentId, counselor, counselorStatus, sortTime, pageable))
                        .map(payment -> PaymentGetCounselorResponse.of(payment, finalTotal));

        return page.getContent();
    }

    @Transactional
    @Override
    public void updateSettlementOngoingByCounselor(Long paymentId, Long customerId) {
        Counselor counselor = counselorService.getCounselorByCustomerId(customerId);
        Payment payment = getPaymentByPaymentId(paymentId);
        payment.checkUpdateAuthorityByCounselor(counselor.getCounselorId());
        if ((payment.getCounselorStatus() == null) ||
                (!payment.getCounselorStatus().equals(PaymentCounselorStatus.SETTLEMENT_WAITING))) {
            throw new PaymentException(PaymentErrorCode.INVALID_SETTLEMENT_ONGOING, paymentId.toString());
        }

        payment.updateCounselorStatusSettlementOngoing();
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

    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkPaymentConsultFinishToSettlementWaiting() {
        paymentRepository.findAllByCounselorStatusAndIsActivatedIsTrue(PaymentCounselorStatus.CONSULT_FINISH).stream()
                .filter(payment -> payment.getUpdatedAt().plusDays(PAYMENT_FIXED_OFFSET).isBefore(LocalDateTime.now()))
                .forEach(Payment::updateCounselorStatusSettlementWaiting);

        counselorService.getAllCounselors()
                .forEach(this::updateSettlement);
    }

    @Transactional
    public void updateSettlement(Counselor counselor) {
        Settlement settlement = counselor.getSettlement();
        settlement.clearAll();
        paymentRepository.findAllByConsultCounselorAndCounselorStatusAndIsActivatedIsTrue(counselor,
                PaymentCounselorStatus.SETTLEMENT_WAITING)
                .forEach(payment -> {
                    Long amount = payment.getConsult().getCost() - FEE;
                    settlement.updateWaitingAll(amount);
                    if (payment.getUpdatedAt().plusWeeks(1).isBefore(LocalDateTime.now())) {
                        settlement.updateWaitingWeek(amount);
                    } else if (payment.getUpdatedAt().plusMonths(1).isBefore(LocalDateTime.now())) {
                        settlement.updateWaitingMonth(amount);
                    }
                });
        paymentRepository.findAllByConsultCounselorAndCounselorStatusAndIsActivatedIsTrue(counselor,
                PaymentCounselorStatus.SETTLEMENT_ONGOING)
                .forEach(payment -> {
                    Long amount = payment.getConsult().getCost() - FEE;
                    settlement.updateOngoingAll(amount);
                    if (payment.getUpdatedAt().plusWeeks(1).isBefore(LocalDateTime.now())) {
                        settlement.updateOngoingWeek(amount);
                    } else if (payment.getUpdatedAt().plusMonths(1).isBefore(LocalDateTime.now())) {
                        settlement.updateOngoingMonth(amount);
                    }
                });
        paymentRepository.findAllByConsultCounselorAndCounselorStatusAndIsActivatedIsTrue(counselor,
                PaymentCounselorStatus.SETTLEMENT_COMPLETE)
                .forEach(payment -> {
                    Long amount = payment.getConsult().getCost() - FEE;
                    settlement.updateCompleteAll(amount);
                    if (payment.getUpdatedAt().plusWeeks(1).isBefore(LocalDateTime.now())) {
                        settlement.updateCompleteWeek(amount);
                    } else if (payment.getUpdatedAt().plusMonths(1).isBefore(LocalDateTime.now())) {
                        settlement.updateCompleteMonth(amount);
                    }
                });
    }
}
