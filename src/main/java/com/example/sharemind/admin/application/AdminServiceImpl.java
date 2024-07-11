package com.example.sharemind.admin.application;

import com.example.sharemind.admin.dto.response.ConsultGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.CounselorGetByNicknameOrEmailResponse;
import com.example.sharemind.admin.dto.response.CustomerGetByNicknameOrEmailResponse;
import com.example.sharemind.admin.dto.response.InformationGetResponse;
import com.example.sharemind.admin.dto.response.PaymentGetRefundWaitingResponse;
import com.example.sharemind.admin.dto.response.PaymentGetSettlementOngoingResponse;
import com.example.sharemind.admin.dto.response.PostGetByIdResponse;
import com.example.sharemind.admin.dto.response.PostGetUnpaidPrivateResponse;
import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.content.ChatStatus;
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
import com.example.sharemind.email.application.EmailService;
import com.example.sharemind.email.content.EmailType;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.content.LetterStatus;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.payment.application.PaymentService;
import com.example.sharemind.payment.content.PaymentCounselorStatus;
import com.example.sharemind.payment.content.PaymentCustomerStatus;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.payment.exception.PaymentErrorCode;
import com.example.sharemind.payment.exception.PaymentException;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.content.PostStatus;
import com.example.sharemind.post.domain.Post;
import com.example.sharemind.post.exception.PostErrorCode;
import com.example.sharemind.post.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private static final String SHUT_DOWN_KEY = "SHUT_DOWN";

    private final ConsultService consultService;
    private final LetterService letterService;
    private final ChatService chatService;
    private final PaymentService paymentService;
    private final CounselorService counselorService;
    private final CustomerService customerService;
    private final PostService postService;
    private final EmailService emailService;
    private final RedisTemplate<String, Boolean> redisTemplate;

    @Override
    public List<ConsultGetUnpaidResponse> getUnpaidConsults() {
        return consultService.getUnpaidConsults().stream()
                .map(ConsultGetUnpaidResponse::of)
                .toList();
    }

    @Transactional
    public void updateConsultIsPaid(Long consultId) {
        Consult consult = consultService.getConsultByConsultId(consultId);
        if (consult.getPayment().getIsPaid()) {
            throw new ConsultException(ConsultErrorCode.CONSULT_ALREADY_PAID, consultId.toString());
        }

        switch (consult.getConsultType()) {
            case LETTER -> {
                Letter letter = letterService.createLetter();

                consult.updateIsPaidAndLetter(letter);
            }
            case CHAT -> chatService.createChat(consult);
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
            throw new CounselorException(CounselorErrorCode.COUNSELOR_NOT_IN_EVALUATION,
                    counselorId.toString());
        }

        ProfileStatus profileStatus;
        String email = customerService.getCustomerByCounselor(counselor).getEmail();
        if (isPassed) {
            profileStatus = ProfileStatus.EVALUATION_COMPLETE;
            emailService.sendEmail(email, EmailType.COUNSELOR_PROFILE_COMPLETE, "");
        } else {
            profileStatus = ProfileStatus.EVALUATION_FAIL;
            emailService.sendEmail(email, EmailType.COUNSELOR_PROFILE_FAIL, "");
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
        if (!payment.getCustomerStatus().equals(PaymentCustomerStatus.REFUND_WAITING)) {
            throw new PaymentException(PaymentErrorCode.INVALID_REFUND_COMPLETE);
        }

        payment.updateCustomerStatusRefundComplete();
    }

    @Override
    public List<PaymentGetSettlementOngoingResponse> getSettlementOngoingPayments() {
        return paymentService.getSettlementOngoingPayments().stream()
                .map(PaymentGetSettlementOngoingResponse::of)
                .toList();
    }

    @Transactional
    @Override
    public void updateSettlementComplete(Long paymentId) {
        Payment payment = paymentService.getPaymentByPaymentId(paymentId);
        if (!payment.getCounselorStatus().equals(PaymentCounselorStatus.SETTLEMENT_ONGOING)) {
            throw new PaymentException(PaymentErrorCode.INVALID_SETTLEMENT_COMPLETE);
        }

        payment.updateCounselorStatusSettlementComplete();
    }

    @Override
    public List<PostGetUnpaidPrivateResponse> getUnpaidPrivatePosts() {
        return postService.getUnpaidPrivatePosts().stream()
                .map(PostGetUnpaidPrivateResponse::of)
                .toList();
    }

    @Transactional
    @Override
    public void updatePostIsPaid(Long postId) {
        Post post = postService.getPostByPostId(postId);
        if (post.getIsPaid()) {
            throw new PostException(PostErrorCode.POST_ALREADY_PAID, postId.toString());
        }

        post.updateIsPaid();
    }

    @Override
    public List<CustomerGetByNicknameOrEmailResponse> getCustomersByNicknameOrEmail(
            String keyword) {
        return customerService.getCustomersByNicknameOrEmail(keyword).stream()
                .map(CustomerGetByNicknameOrEmailResponse::of)
                .toList();
    }

    @Transactional
    @Override
    public void updateCustomerIsBanned(Long customerId, Boolean isBanned) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        customer.updateIsBanned(isBanned);
    }

    @Override
    public List<CounselorGetByNicknameOrEmailResponse> getCounselorsByNicknameOrEmail(
            String keyword) {
        return counselorService.getCounselorsByNicknameOrEmail(keyword).stream()
                .map(counselor -> CounselorGetByNicknameOrEmailResponse.of(counselor,
                        customerService.getCustomerByCounselor(counselor).getEmail()))
                .toList();
    }

    @Transactional
    @Override
    public void updateCounselorPending(Long counselorId) {
        Counselor counselor = counselorService.getCounselorByCounselorId(counselorId);
        counselor.updateProfileStatusAndProfileUpdatedAt(ProfileStatus.EVALUATION_PENDING);
    }

    @Override
    public PostGetByIdResponse getPostByPostId(Long postId) {
        Post post = postService.getPostByPostId(postId);
        return PostGetByIdResponse.of(post);
    }

    @Transactional
    @Override
    public void deletePostByPostId(Long postId) {
        Post post = postService.getPostByPostId(postId);
        post.updateIsActivatedFalse();
    }

    @Override
    public InformationGetResponse getInformation() {
        long customers = customerService.countAllCustomers();

        List<Counselor> counselors = counselorService.getAllCounselors();
        long completedCounselors = 0, waitingCounselors = 0;
        for (Counselor counselor : counselors) {
            ProfileStatus profileStatus = counselor.getProfileStatus();

            if (profileStatus.equals(ProfileStatus.EVALUATION_COMPLETE)) {
                completedCounselors++;
            } else if (profileStatus.equals(ProfileStatus.EVALUATION_PENDING)) {
                waitingCounselors++;
            }
        }

        List<Chat> chats = chatService.getAllChats();
        long completedChats = 0, completedChatCosts = 0, canceledChats = 0, canceledChatCosts = 0;
        for (Chat chat : chats) {
            ChatStatus chatStatus = chat.getChatStatus();

            if (chatStatus.equals(ChatStatus.FINISH)) {
                completedChats++;
                completedChatCosts += chat.getConsult().getCost();
            } else if (chatStatus.equals(ChatStatus.COUNSELOR_CANCEL) || chatStatus.equals(
                    ChatStatus.CUSTOMER_CANCEL)) {
                canceledChats++;
                canceledChatCosts += chat.getConsult().getCost();
            }
        }

        List<Letter> letters = letterService.getAllLetters();
        long completedLetters = 0, completedLetterCosts = 0, canceledLetters = 0, canceledLetterCosts = 0;
        for (Letter letter : letters) {
            LetterStatus letterStatus = letter.getLetterStatus();

            if (letterStatus.equals(LetterStatus.FIRST_FINISH) || letterStatus.equals(
                    LetterStatus.SECOND_FINISH)) {
                completedLetters++;
                completedLetterCosts += letter.getConsult().getCost();
            } else if (letterStatus.equals(LetterStatus.CUSTOMER_CANCEL) || letterStatus.equals(
                    LetterStatus.COUNSELOR_CANCEL)) {
                canceledLetters++;
                canceledLetterCosts += letter.getConsult().getCost();
            }
        }

        List<Post> posts = postService.getAllPosts();
        long publicPosts = 0, completedPublicPosts = 0, secretPosts = 0, secretPostCosts = 0,
                completedSecretPosts = 0, completedSecretPostCosts = 0;
        for (Post post : posts) {
            boolean isPublic = post.getIsPublic();
            PostStatus postStatus = post.getPostStatus();

            if (isPublic) {
                publicPosts++;
                if (postStatus.equals(PostStatus.TIME_OUT) || postStatus.equals(
                        PostStatus.COMPLETED)) {
                    completedPublicPosts++;
                }
            } else {
                secretPosts++;
                secretPostCosts += post.getCost();
                if (postStatus.equals(PostStatus.TIME_OUT) || postStatus.equals(
                        PostStatus.COMPLETED)) {
                    completedSecretPosts++;
                    completedSecretPostCosts += post.getCost();
                }
            }
        }

        return InformationGetResponse.of(customers, completedCounselors, waitingCounselors,
                completedChats, completedChatCosts, canceledChats, canceledChatCosts,
                completedLetters, completedLetterCosts, canceledLetters, canceledLetterCosts,
                publicPosts, completedPublicPosts, secretPosts, secretPostCosts,
                completedSecretPosts, completedSecretPostCosts);
    }

    @Override
    public Boolean updateShutdown(Boolean shutdown) {
        ValueOperations<String, Boolean> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(SHUT_DOWN_KEY, shutdown);

        return valueOperations.get(SHUT_DOWN_KEY);
    }
}
