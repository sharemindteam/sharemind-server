package com.example.sharemind.nonRealtimeMessage.presentation;

import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.nonRealtimeMessage.application.NonRealtimeMessageService;
import com.example.sharemind.nonRealtimeMessage.dto.request.NonRealtimeMessageCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/nonRealtimeMessages")
@RequiredArgsConstructor
public class NonRealtimeMessageController {

    private final NonRealtimeMessageService nonRealtimeMessageService;

    @PostMapping
    public ResponseEntity<Void> createNonRealtimeMessage(@Valid @RequestBody NonRealtimeMessageCreateRequest nonRealtimeMessageCreateRequest,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        nonRealtimeMessageService.createNonRealtimeMessage(nonRealtimeMessageCreateRequest, customUserDetails.getCustomer());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
