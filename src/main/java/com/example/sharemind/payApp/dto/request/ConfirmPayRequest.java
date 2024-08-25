package com.example.sharemind.payApp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ConfirmPayRequest {

    @JsonProperty("userid")
    private String userId;

    @JsonProperty("linkkey")
    private String key;

    @JsonProperty("linkval")
    private String value;

    @JsonProperty("price")
    private Long cost;

    @JsonProperty("pay_date")
    private String approvedAt;

    @JsonProperty("pay_type")
    private Integer method;

    @JsonProperty("pay_state")
    private Integer state;

    @JsonProperty("val1")
    private Long val1;

    @JsonProperty("mul_no")
    private String payAppId;
}
