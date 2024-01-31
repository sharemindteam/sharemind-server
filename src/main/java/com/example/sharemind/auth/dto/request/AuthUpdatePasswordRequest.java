package com.example.sharemind.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AuthUpdatePasswordRequest {

    @Schema(description = "새 비밀번호")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp = "^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\\d~!@#$%^&*()_+=]{10,}$",
            message = "비밀번호는 영문, 숫자, 특수문자 중 2가지 이상을 조합한 10자리 이상의 문자열이어야 합니다.")
    private String password;
}
