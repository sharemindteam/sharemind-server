package com.example.sharemind.counselor.dto.response;

import com.example.sharemind.counselor.domain.Counselor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CounselorGetAccountResponse {

    @Schema(description = "계좌번호", example = "1002961686868")
    private final String account;

    @Schema(description = "은행", example = "우리은행")
    private final String bank;

    @Schema(description = "예금주", example = "김뫄뫄")
    private final String accountHolder;

    @Builder
    public CounselorGetAccountResponse(String account, String bank, String accountHolder) {
        this.account = account;
        this.bank = bank;
        this.accountHolder = accountHolder;
    }

    public static CounselorGetAccountResponse of(Counselor counselor) {
        return CounselorGetAccountResponse.builder()
                .account(counselor.getAccount())
                .bank(counselor.getBank())
                .accountHolder(counselor.getAccountHolder())
                .build();
    }
}
