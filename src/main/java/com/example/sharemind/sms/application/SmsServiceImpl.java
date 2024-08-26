package com.example.sharemind.sms.application;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.sms.dto.response.SmsGetResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private static final String BASE_URL = "https://apis.aligo.in";
    private static final String COUNSELOR_MESSAGE = "새로운 상담 신청이 도착했습니다.";

    private final CounselorService counselorService;

    @Value("${spring.sms.key}")
    private String key;

    @Value("${spring.sms.id}")
    private String id;

    @Value("${spring.sms.sender}")
    private String sender;

    private final RestClient restClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .build();
    private final ObjectMapper objectMapper;

    @Override
    public SmsGetResponse sendSmsCounselor(Long counselorId) {
        Counselor counselor = counselorService.getCounselorByCounselorId(counselorId);
        return sendSms(counselor.getPhoneNumber(), COUNSELOR_MESSAGE);
    }

    private SmsGetResponse sendSms(String phoneNumber, String message) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("key", key);
        body.add("user_id", id);
        body.add("sender", sender);
        body.add("receiver", phoneNumber);
        body.add("msg", message);
//        body.add("testmode_yn", "Y");

        String response = restClient.post()
                .uri("/send/")
                .body(body).retrieve()
                .body(String.class);
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            int resultCode = rootNode.get("result_code").asInt();
            String responseMessage = rootNode.get("message").asText();
            return SmsGetResponse.of(resultCode, responseMessage);
        } catch (Exception e) {
            throw new RuntimeException("JSON 처리 중 오류 발생", e);
        }
    }
}
