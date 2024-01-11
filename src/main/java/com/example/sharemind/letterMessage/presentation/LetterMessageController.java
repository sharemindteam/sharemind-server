package com.example.sharemind.letterMessage.presentation;

import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.letterMessage.application.LetterMessageService;
import com.example.sharemind.letterMessage.dto.request.LetterMessageCreateRequest;
import com.example.sharemind.letterMessage.dto.request.LetterMessageGetIsSavedRequest;
import com.example.sharemind.letterMessage.dto.request.LetterMessageUpdateRequest;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetIsSavedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/letterMessages")
@RequiredArgsConstructor
public class LetterMessageController {

    private final LetterMessageService letterMessageService;

    @PostMapping
    public ResponseEntity<Void> createLetterMessage(@Valid @RequestBody LetterMessageCreateRequest letterMessageCreateRequest,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.createLetterMessage(letterMessageCreateRequest, customUserDetails.getCustomer());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateLetterMessage(@Valid @RequestBody LetterMessageUpdateRequest letterMessageUpdateRequest,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.updateLetterMessage(letterMessageUpdateRequest, customUserDetails.getCustomer());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<LetterMessageGetIsSavedResponse> getIsSaved(@Valid @RequestBody LetterMessageGetIsSavedRequest letterMessageGetIsSavedRequest) {
        return ResponseEntity.ok(letterMessageService.getIsSaved(letterMessageGetIsSavedRequest));
    }
}
