package com.example.sharemind.admin.dto.response;

import com.example.sharemind.customer.domain.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerGetByNicknameOrEmailResponse {

    @Schema(description = "사용자 아이디")
    private final Long customerId;

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "이메일")
    private final String email;

    @Builder
    public CustomerGetByNicknameOrEmailResponse(Long customerId, String nickname, String email) {
        this.customerId = customerId;
        this.nickname = nickname;
        this.email = email;
    }

    public static CustomerGetByNicknameOrEmailResponse of(Customer customer) {
        return CustomerGetByNicknameOrEmailResponse.builder()
                .customerId(customer.getCustomerId())
                .nickname(customer.getNickname())
                .email(customer.getEmail())
                .build();
    }
}
