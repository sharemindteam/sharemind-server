package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.PaymentGetRefundWaitingResponse;
import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.exception.ConsultErrorCode;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.content.ProfileStatus;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.counselor.dto.response.CounselorGetProfileResponse;
import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.content.Role;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.payment.application.PaymentService;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.payment.exception.PaymentErrorCode;
import com.example.sharemind.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ConsultService consultService;
    private final LetterService letterService;
    private final ChatService chatService;
    private final PaymentService paymentService;
    private final CounselorService counselorService;
    private final CustomerService customerService;

    @Override
    public List<ConsultGetUnpaidResponse> getUnpaidConsults() {
        return consultService.getUnpaidConsults().stream()
                .map(ConsultGetUnpaidResponse::of)
                .toList();
    }

    @Transactional
    public void updateIsPaid(Long consultId) {
        Consult consult = consultService.getConsultByConsultId(consultId);
        if (consult.getPayment().getIsPaid()) {
            throw new ConsultException(ConsultErrorCode.CONSULT_ALREADY_PAID, consultId.toString());
        }

        switch (consult.getConsultType()) {
            case LETTER -> {
                Letter letter = letterService.createLetter();

                consult.updateIsPaidAndLetter(letter);
            }
            case CHAT -> {
                Chat chat = chatService.createChat(consult);

                consult.updateIsPaidAndChat(chat);
            }
        }
    }

    @Override
    public List<CounselorGetProfileResponse> getPendingCounselors() {
        return counselorService.getEvaluationPendingConsults().stream()
                .map(CounselorGetProfileResponse::of)
                .toList();
    }

    @Transactional
    @Override
    public void updateProfileStatus(Long counselorId, Boolean isPassed) {
        Counselor counselor = counselorService.getCounselorByCounselorId(counselorId);
        if ((counselor.getProfileStatus() == null) ||
                (!counselor.getProfileStatus().equals(ProfileStatus.EVALUATION_PENDING))) {
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_IN_EVALUATION, counselorId.toString());
        }

        ProfileStatus profileStatus;
        if (isPassed) {
            profileStatus = ProfileStatus.EVALUATION_COMPLETE;
        } else {
            profileStatus = ProfileStatus.EVALUATION_FAIL;
        }
        counselor.updateProfileStatusAndProfileUpdatedAt(profileStatus);

        if (counselor.getProfileStatus().equals(ProfileStatus.EVALUATION_COMPLETE)) {
            Customer customer = customerService.getCustomerByCounselor(counselor);
            if (!customer.getRoles().contains(Role.ROLE_COUNSELOR)) {
                customer.addRole(Role.ROLE_COUNSELOR);
            }
        }
    }

    @Override
    public List<PaymentGetRefundWaitingResponse> getRefundWaitingPayments() {
        return paymentService.getRefundWaitingPayments().stream()
                .map(PaymentGetRefundWaitingResponse::of)
                .toList();
    }

    @Transactional
    @Override
    public void updateRefundComplete(Long paymentId) {
        Payment payment = paymentService.getPaymentByPaymentId(paymentId);
        if ((payment.getCustomerStatus() == null) ||
                (!payment.getCustomerStatus().equals(PaymentCustomerStatus.REFUND_WAITING))) {
            throw new PaymentException(PaymentErrorCode.INVALID_REFUND_COMPLETE);
        }

        payment.updatePaymentCustomerStatus(PaymentCustomerStatus.REFUND_COMPLETE);
    }
}
