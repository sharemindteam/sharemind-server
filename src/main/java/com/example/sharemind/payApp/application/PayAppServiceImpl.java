package com.example.sharemind.payApp.application;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.payApp.content.PayMethod;
import com.example.sharemind.payApp.dto.request.ConfirmPayRequest;
import com.example.sharemind.payApp.exception.PayAppErrorCode;
import com.example.sharemind.payApp.exception.PayAppException;
import com.example.sharemind.payment.application.PaymentService;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.domain.Post;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PayAppServiceImpl implements PayAppService {

    private static final String PAY_APP_URL = "https://api.payapp.kr/oapi/apiLoad.html";
    private static final String PAY_REQUEST_CMD = "payrequest";
    private static final String CHECK_RETRY_Y = "y";
    private static final String SECRET_POST = "공개상담 - 비공개";

    private final PaymentService paymentService;
    private final PostService postService;
    private final ChatService chatService;
    private final LetterService letterService;

    private final RestClient restClient = RestClient.builder()
            .baseUrl(PAY_APP_URL)
            .build();

    @Value("${payapp.user-id}")
    private String payAppUserId;

    @Value("${payapp.key}")
    private String payAppKey;

    @Value("${payapp.value}")
    private String payAppValue;

    @Value("${payapp.feedback.consult}")
    private String feedBackConsult;

    @Value("${payapp.feedback.post}")
    private String feedBackPost;

    @Value("${payapp.return}")
    private String returnUrl;

    @Override
    @Transactional
    public String payConsult(Long paymentId) {
        Payment payment = paymentService.getPaymentByPaymentId(paymentId);
        if (payment.checkAlreadyPaid()) {
            throw new PayAppException(PayAppErrorCode.ALREADY_REQUESTED_PAYMENT);
        }

        Consult consult = payment.getConsult();

        String result = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("cmd", PAY_REQUEST_CMD)
                        .queryParam("userid", payAppUserId)
                        .queryParam("goodname", consult.getConsultType().getDisplayName())
                        .queryParam("price", consult.getCost())
                        .queryParam("recvphone", payment.getCustomerPhoneNumber())
                        .queryParam("feedbackurl", feedBackConsult)
                        .queryParam("val1", payment.getPaymentId())
                        .queryParam("returnurl", returnUrl)
                        .queryParam("checkretry", CHECK_RETRY_Y)
                        .build())
                .retrieve()
                .body(String.class);

        if (result == null) {
            throw new PayAppException(PayAppErrorCode.PAY_RESPONSE_NOT_FOUND);
        }

        Map<String, String> response = parseQueryString(result);

        String state = response.get("state");
        if (state.equals("1")) {
            String payAppId = response.get("mul_no");
            payment.updatePayAppId(payAppId);

            return response.get("payurl");
        } else if (state.equals("0")) {
            String errorNumber = response.get("errno");
            String errorMessage = response.get("errorMessage");

            throw new PayAppException(PayAppErrorCode.PAY_REQUEST_FAIL,
                    errorNumber + " " + errorMessage);
        } else {
            throw new PayAppException(PayAppErrorCode.PAY_REQUEST_FAIL);
        }
    }

    @Override
    @Transactional
    public String payPost(Long postId) {
        Post post = postService.getPostByPostId(postId);
        if (post.checkAlreadyPaid()) {
            throw new PayAppException(PayAppErrorCode.ALREADY_REQUESTED_PAYMENT);
        }

        String result = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("cmd", PAY_REQUEST_CMD)
                        .queryParam("userid", payAppUserId)
                        .queryParam("goodname", SECRET_POST)
                        .queryParam("price", post.getCost())
                        .queryParam("recvphone", post.getCustomerPhoneNumber())
                        .queryParam("feedbackurl", feedBackPost)
                        .queryParam("val1", post.getPostId())
                        .queryParam("returnurl", returnUrl)
                        .queryParam("checkretry", CHECK_RETRY_Y)
                        .build())
                .retrieve()
                .body(String.class);

        if (result == null) {
            throw new PayAppException(PayAppErrorCode.PAY_RESPONSE_NOT_FOUND);
        }

        Map<String, String> response = parseQueryString(result);

        String state = response.get("state");
        if (state.equals("1")) {
            String payAppId = response.get("mul_no");
            post.updatePayAppId(payAppId);

            return response.get("payurl");
        } else if (state.equals("0")) {
            String errorNumber = response.get("errno");
            String errorMessage = response.get("errorMessage");

            throw new PayAppException(PayAppErrorCode.PAY_REQUEST_FAIL,
                    errorNumber + " " + errorMessage);
        } else {
            throw new PayAppException(PayAppErrorCode.PAY_REQUEST_FAIL);
        }
    }

    @Override
    @Transactional
    public String confirmConsult(String userId, String key, String value, Long cost,
            String approvedAt, Integer method, Integer state, Long val1, String payAppId) {
        if (!payAppUserId.equals(userId) || !payAppKey.equals(key) || !payAppValue.equals(value)) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_BASIC_INFO_FAIL);
        }

        Payment payment = paymentService.getPaymentByPaymentId(val1);
        if (!payment.getPayAppId().equals(payAppId) || !payment.getConsult().getCost()
                .equals(cost)) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_PAYMENT_INFO_FAIL);
        }

        if (state == 4 && !payment.getIsPaid()) {
            PayMethod payMethod = PayMethod.getPayMethod(method);

            Consult consult = payment.getConsult();
            switch (consult.getConsultType()) {
                case LETTER -> {
                    Letter letter = letterService.createLetter();

                    consult.updateIsPaidAndLetter(letter, payMethod.getMethod(), approvedAt);
                }
                case CHAT -> {
                    Chat chat = chatService.createChat(consult);

                    consult.updateIsPaidAndChat(chat, payMethod.getMethod(), approvedAt);
                }
            }
        }

        return "SUCCESS";
    }

    @Override
    @Transactional
    public String confirmPost(String userId, String key, String value, Long cost,
            String approvedAt, Integer method, Integer state, Long val1, String payAppId) {
        if (!payAppUserId.equals(userId) || !payAppKey.equals(key) || !payAppValue.equals(value)) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_BASIC_INFO_FAIL);
        }

        Post post = postService.getPostByPostId(val1);
        if (!post.getPayAppId().equals(payAppId) || !post.getCost().equals(cost)) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_PAYMENT_INFO_FAIL);
        }

        if (state == 4 && !post.getIsPaid()) {
            PayMethod payMethod = PayMethod.getPayMethod(method);

            post.updateMethodAndIsPaidAndApprovedAt(payMethod.getMethod(), approvedAt);
        }

        return "SUCCESS";
    }

    @Override
    @Transactional
    public String confirmConsult(ConfirmPayRequest confirmPayRequest) {
        if (!payAppUserId.equals(confirmPayRequest.getUserId()) || !payAppKey.equals(
                confirmPayRequest.getKey()) || !payAppValue.equals(confirmPayRequest.getValue())) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_BASIC_INFO_FAIL);
        }

        Payment payment = paymentService.getPaymentByPaymentId(confirmPayRequest.getVal1());
        if (!payment.getPayAppId().equals(confirmPayRequest.getPayAppId()) || !payment.getConsult()
                .getCost().equals(confirmPayRequest.getCost())) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_PAYMENT_INFO_FAIL);
        }

        if (confirmPayRequest.getState() == 4 && !payment.getIsPaid()) {
            PayMethod payMethod = PayMethod.getPayMethod(confirmPayRequest.getMethod());

            Consult consult = payment.getConsult();
            switch (consult.getConsultType()) {
                case LETTER -> {
                    Letter letter = letterService.createLetter();

                    consult.updateIsPaidAndLetter(letter, payMethod.getMethod(),
                            confirmPayRequest.getApprovedAt());
                }
                case CHAT -> {
                    Chat chat = chatService.createChat(consult);

                    consult.updateIsPaidAndChat(chat, payMethod.getMethod(),
                            confirmPayRequest.getApprovedAt());
                }
            }
        }

        return "SUCCESS";
    }

    @Override
    @Transactional
    public String confirmPost(ConfirmPayRequest confirmPayRequest) {
        if (!payAppUserId.equals(confirmPayRequest.getUserId()) || !payAppKey.equals(
                confirmPayRequest.getKey()) || !payAppValue.equals(confirmPayRequest.getValue())) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_BASIC_INFO_FAIL);
        }

        Post post = postService.getPostByPostId(confirmPayRequest.getVal1());
        if (!post.getPayAppId().equals(confirmPayRequest.getPayAppId()) || !post.getCost()
                .equals(confirmPayRequest.getCost())) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_PAYMENT_INFO_FAIL);
        }

        if (confirmPayRequest.getState() == 4 && !post.getIsPaid()) {
            PayMethod payMethod = PayMethod.getPayMethod(confirmPayRequest.getMethod());

            post.updateMethodAndIsPaidAndApprovedAt(payMethod.getMethod(),
                    confirmPayRequest.getApprovedAt());
        }

        return "SUCCESS";
    }

    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> map = new HashMap<>();

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? pair.substring(0, idx) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : "";

            String decodedKey = URLDecoder.decode(key, StandardCharsets.UTF_8);
            String decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8);

            map.put(decodedKey, decodedValue);
        }

        return map;
    }
}
