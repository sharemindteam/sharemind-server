package com.example.sharemind.payApp.presentation;

import com.example.sharemind.payApp.application.PayAppService;
import com.example.sharemind.payApp.dto.request.ConfirmPayRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payApp")
@RequiredArgsConstructor
public class PayAppController {

    private final PayAppService payAppService;

    @PostMapping("/consults")
    public String confirmConsult(@RequestBody ConfirmPayRequest confirmPayRequest) {
        return payAppService.confirmConsult(confirmPayRequest);
    }

    @PostMapping("/posts")
    public void testConfirmPost(HttpServletRequest request) throws IOException {
        payAppService.test(request);
    }

//    @PostMapping("/posts")
//    public String confirmPost(@RequestBody ConfirmPayRequest confirmPayRequest) {
//        return payAppService.confirmPost(confirmPayRequest);
//    }

//    @PostMapping("/consults")
//    public String confirmConsult(@RequestParam("userid") String userId,
//            @RequestParam("linkkey") String key, @RequestParam("linkval") String value,
//            @RequestParam("price") Long cost, @RequestParam("pay_date") String approvedAt,
//            @RequestParam("pay_type") Integer method, @RequestParam("pay_state") Integer state,
//            @RequestParam("val1") Long val1, @RequestParam("mul_no") String payAppId) {
//        return payAppService.confirmConsult(userId, key, value, cost, approvedAt, method, state,
//                val1, payAppId);
//    }
//
//    @PostMapping("/posts")
//    public String confirmPost(@RequestParam("userid") String userId,
//            @RequestParam("linkkey") String key, @RequestParam("linkval") String value,
//            @RequestParam("price") Long cost, @RequestParam("pay_date") String approvedAt,
//            @RequestParam("pay_type") Integer method, @RequestParam("pay_state") Integer state,
//            @RequestParam("val1") Long val1, @RequestParam("mul_no") String payAppId) {
//        return payAppService.confirmPost(userId, key, value, cost, approvedAt, method, state,
//                val1, payAppId);
//    }
}
