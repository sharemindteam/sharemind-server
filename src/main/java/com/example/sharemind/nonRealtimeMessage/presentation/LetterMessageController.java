package com.example.sharemind.nonRealtimeMessage.presentation;

import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.nonRealtimeMessage.application.LetterMessageService;
import com.example.sharemind.nonRealtimeMessage.dto.request.LetterMessageCreateRequest;
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
public class LetterMessageController {

    private final LetterMessageService letterMessageService;

    @PostMapping
    public ResponseEntity<Void> createLetterMessage(@Valid @RequestBody LetterMessageCreateRequest letterMessageCreateRequest,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.createLetterMessage(letterMessageCreateRequest, customUserDetails.getCustomer());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
