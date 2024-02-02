package com.example.sharemind.counselor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CounselorUpdateAccountRequest {

    @Schema(description = "계좌번호", example = "1002961686868")
    @NotBlank(message = "계좌번호는 공백일 수 없습니다.")
    private String account;

    @Schema(description = "은행", example = "우리은행")
    @NotBlank(message = "은행은 공백일 수 없습니다.")
    private String bank;

    @Schema(description = "예금주", example = "김뫄뫄")
    @NotBlank(message = "예금주는 공백일 수 없습니다.")
    private String accountHolder;
}
