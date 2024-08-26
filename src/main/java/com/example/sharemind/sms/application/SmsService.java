package com.example.sharemind.sms.application;

import com.example.sharemind.sms.dto.response.SmsGetResponse;

public interface SmsService {

    SmsGetResponse sendSmsCounselor(Long counselorId);
}
