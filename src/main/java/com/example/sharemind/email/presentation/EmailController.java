package com.example.sharemind.email.presentation;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.email.application.EmailService;
import com.example.sharemind.email.dto.request.EmailPostCodeRequest;
import com.example.sharemind.email.dto.request.EmailPostRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody EmailPostRequest emailPostRequest) {
        customerService.checkDuplicateEmail(emailPostRequest.getEmail());

        emailService.sendVerificationCode(emailPostRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/code")
    public ResponseEntity<Void> verifyCode(@Valid @RequestBody EmailPostCodeRequest emailPostCodeRequest) {
        emailService.verifyCode(emailPostCodeRequest.getEmail(), emailPostCodeRequest.getCode());
        return ResponseEntity.ok().build();
    }
}
