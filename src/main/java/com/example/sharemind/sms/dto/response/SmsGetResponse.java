package com.example.sharemind.sms.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsGetResponse {

    private final int result_code;
    private final String message;
    private final Integer msg_id;
    private final Integer success_cnt;
    private final Integer error_cnt;
    private final String msg_type;

    public static SmsGetResponse of(int resultCode, String message, Integer msgId,
            Integer successCnt, Integer errorCnt, String msgType) {
        return new SmsGetResponse(resultCode, message, msgId, successCnt, errorCnt, msgType);
    }

    public static SmsGetResponse of(int resultCode, String message) {
        return new SmsGetResponse(resultCode, message, null, 0, 1, null);
    }
}
