package com.example.sharemind.email.presentation;

import com.example.sharemind.auth.application.AuthService;
import com.example.sharemind.email.application.EmailService;
import com.example.sharemind.email.dto.request.EmailPostCodeRequest;
import com.example.sharemind.email.dto.request.EmailPostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email Controller", description = "이메일 컨트롤러")
@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final AuthService authService;

    @Operation(summary = "이메일 인증코드 발송", description = "회원가입 시 이메일 인증코드를 발송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "해당 이메일로 인증 코드 발송 성공"),
            @ApiResponse(responseCode = "400", description = "1. 이미 회원으로 등록된 이메일\n 2. 5분 내에 코드 전송 5회 횟수를 초과 했을 경우\n3. 올바르지 않은 이메일 형식")
    })
    @PostMapping
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody EmailPostRequest emailPostRequest) {
        authService.checkDuplicateEmail(emailPostRequest.getEmail());

        emailService.sendVerificationCode(emailPostRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원가입 이메일 인증코드 검증", description = "회원가입 이메일 인증코드가 일치하는지 검증")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검증 성공"),
            @ApiResponse(responseCode = "400", description = "1. 잘못된 인증코드\n2. 올바르지 않은 이메일 형식")
    })
    @PostMapping("/code")
    public ResponseEntity<Void> verifyCode(@Valid @RequestBody EmailPostCodeRequest emailPostCodeRequest) {
        emailService.verifyCode(emailPostCodeRequest.getEmail(), emailPostCodeRequest.getCode());
        return ResponseEntity.ok().build();
    }
}
