package com.example.sharemind.payApp.application;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.letter.application.LetterService;
import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.payApp.content.PayMethod;
import com.example.sharemind.payApp.exception.PayAppErrorCode;
import com.example.sharemind.payApp.exception.PayAppException;
import com.example.sharemind.payment.application.PaymentService;
import com.example.sharemind.payment.domain.Payment;
import com.example.sharemind.post.application.PostService;
import com.example.sharemind.post.domain.Post;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    public String confirmConsult(HttpServletRequest request) {
        String userId = request.getParameter("userid");
        String key = URLDecoder.decode(request.getParameter("linkkey"), StandardCharsets.UTF_8);
        String value = URLDecoder.decode(request.getParameter("linkval"), StandardCharsets.UTF_8);
        String payAppId = request.getParameter("mul_no");
        Long cost = Long.parseLong(request.getParameter("price"));
        int state = Integer.parseInt(request.getParameter("pay_state"));
        int method = Integer.parseInt(request.getParameter("pay_type"));
        LocalDateTime approvedAt = parseToLocalDateTime(
                URLDecoder.decode(request.getParameter("pay_date"), StandardCharsets.UTF_8));

        if (!payAppUserId.equals(userId) || !payAppKey.equals(key) || !payAppValue.equals(value)) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_BASIC_INFO_FAIL, userId + " " + key + " " + value);
        }

        Payment payment = paymentService.getPaymentByPayAppId(payAppId);
        Consult consult = payment.getConsult();
        if (!consult.getCost().equals(cost)) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_PAYMENT_INFO_FAIL);
        }

        if (state == 4 && !payment.getIsPaid()) {
            PayMethod payMethod = PayMethod.getPayMethod(method);

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
    public String confirmPost(HttpServletRequest request) {
        String userId = request.getParameter("userid");
        String key = URLDecoder.decode(request.getParameter("linkkey"), StandardCharsets.UTF_8);
        String value = URLDecoder.decode(request.getParameter("linkval"), StandardCharsets.UTF_8);
        String payAppId = request.getParameter("mul_no");
        Long cost = Long.parseLong(request.getParameter("price"));
        int state = Integer.parseInt(request.getParameter("pay_state"));
        int method = Integer.parseInt(request.getParameter("pay_type"));
        LocalDateTime approvedAt = parseToLocalDateTime(
                URLDecoder.decode(request.getParameter("pay_date"), StandardCharsets.UTF_8));

        if (!payAppUserId.equals(userId) || !payAppKey.equals(key) || !payAppValue.equals(value)) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_BASIC_INFO_FAIL, userId + " " + key + " " + value);
        }

        Post post = postService.getPostByPayAppId(payAppId);
        if (!post.getCost().equals(cost)) {
            throw new PayAppException(PayAppErrorCode.CONFIRM_PAYMENT_INFO_FAIL);
        }

        if (state == 4 && !post.getIsPaid()) {
            PayMethod payMethod = PayMethod.getPayMethod(method);

            post.updateMethodAndIsPaidAndApprovedAt(payMethod.getMethod(), approvedAt);
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

    private LocalDateTime parseToLocalDateTime(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            throw new PayAppException(PayAppErrorCode.DATETIME_PARSE_FAIL, dateTimeString);
        }
    }
}
