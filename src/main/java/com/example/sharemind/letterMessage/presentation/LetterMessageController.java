package com.example.sharemind.letterMessage.presentation;

import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.letterMessage.application.LetterMessageService;
import com.example.sharemind.letterMessage.dto.request.*;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetDeadlineResponse;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetIsSavedResponse;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetResponse;
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

    @PostMapping("/first-question")
    public ResponseEntity<Void> createFirstQuestion(@Valid @RequestBody LetterMessageCreateFirstRequest letterMessageCreateFirstRequest,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.createFirstQuestion(letterMessageCreateFirstRequest, customUserDetails.getCustomer());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/first-question")
    public ResponseEntity<Void> updateFirstQuestion(@Valid @RequestBody LetterMessageUpdateFirstRequest letterMessageUpdateFirstRequest,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.updateFirstQuestion(letterMessageUpdateFirstRequest, customUserDetails.getCustomer());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/drafts/{letterId}")
    public ResponseEntity<LetterMessageGetIsSavedResponse> getIsSaved(@PathVariable Long letterId, @RequestParam String messageType) {
        return ResponseEntity.ok(letterMessageService.getIsSaved(letterId, messageType));
    }

    @GetMapping("/{letterId}")
    public ResponseEntity<LetterMessageGetResponse> getLetterMessage(@PathVariable Long letterId,
                                                                     @RequestParam String messageType, @RequestParam Boolean isCompleted) {
        return ResponseEntity.ok(letterMessageService.getLetterMessage(letterId, messageType, isCompleted));
    }

    @GetMapping("/deadline/{letterId}")
    public ResponseEntity<LetterMessageGetDeadlineResponse> getDeadline(@PathVariable Long letterId, @RequestParam String messageType) {
        return ResponseEntity.ok(letterMessageService.getDeadline(letterId, messageType));
    }
}
