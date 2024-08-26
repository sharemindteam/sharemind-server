package com.example.sharemind.payApp.presentation;

import com.example.sharemind.payApp.application.PayAppService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payApp")
@RequiredArgsConstructor
public class PayAppController {

    private final PayAppService payAppService;

    @PostMapping("/consults")
    public String confirmConsult(HttpServletRequest request) {
        return payAppService.confirmConsult(request);
    }

    @PostMapping("/posts")
    public String confirmPost(HttpServletRequest request) {
        return payAppService.confirmPost(request);
    }
}
