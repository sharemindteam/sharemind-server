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

    @Schema(description = "로그인 제재 여부")
    private final Boolean isBanned;

    @Builder
    public CustomerGetByNicknameOrEmailResponse(Long customerId, String nickname, String email,
            Boolean isBanned) {
        this.customerId = customerId;
        this.nickname = nickname;
        this.email = email;
        this.isBanned = isBanned;
    }

    public static CustomerGetByNicknameOrEmailResponse of(Customer customer) {
        return CustomerGetByNicknameOrEmailResponse.builder()
                .customerId(customer.getCustomerId())
                .nickname(customer.getNickname())
                .email(customer.getEmail())
                .isBanned(customer.getIsBanned())
                .build();
    }
}
