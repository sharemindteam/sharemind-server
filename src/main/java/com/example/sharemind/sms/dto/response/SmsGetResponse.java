package com.example.sharemind.sms.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsGetResponse {

    private final int result_code;
    private final String message;

    public static SmsGetResponse of(int resultCode, String message) {
        return new SmsGetResponse(resultCode, message);
    }
}
