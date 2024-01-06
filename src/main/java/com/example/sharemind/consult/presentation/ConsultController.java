package com.example.sharemind.consult.presentation;

import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.dto.request.ConsultCreateRequest;
import com.example.sharemind.consult.dto.response.ConsultCreateResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consults")
@RequiredArgsConstructor
public class ConsultController {

    private final ConsultService consultService;

    @PostMapping
    public ResponseEntity<ConsultCreateResponse> createConsult(@Valid @RequestBody ConsultCreateRequest consultCreateRequest,
                                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultService.createConsult(consultCreateRequest, customUserDetails.getCustomer()));
    }
}
