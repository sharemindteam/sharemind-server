package com.example.sharemind.consult.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ConsultCreateRequest {

    @NotNull(message = "상담사 id는 공백일 수 없습니다.")
    private Long counselorId;

    @NotBlank(message = "상담 유형은 공백일 수 없습니다.")
    private String consultTypeName;
}
