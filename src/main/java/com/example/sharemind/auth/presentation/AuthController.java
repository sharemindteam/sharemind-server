package com.example.sharemind.auth.presentation;

import com.example.sharemind.auth.application.AuthService;
import com.example.sharemind.auth.dto.request.AuthSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody AuthSignUpRequest authSignUpRequest) {
        authService.signUp(authSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
