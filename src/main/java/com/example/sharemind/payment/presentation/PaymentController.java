package com.example.sharemind.payment.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.payment.application.PaymentService;
import com.example.sharemind.payment.dto.response.PaymentGetCustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Payment Controller", description = "구매자 결제 내역/판매자 수익 관리 컨트롤러")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "구매자 결제 내역 조회", description = "- 구매자 결제 내역 조회\n " +
            "- 주소 형식: /api/v1/payments/customers?status=payment_complete&paymentId=0")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(없으면 빈 배열 반환)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 status",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "status",
                    description = "PAYMENT_COMPLETE(결제 완료), REFUND_WAITING(환불 예정), REFUND_COMPLETE(환불 완료)"),
            @Parameter(name = "paymentId", description = """
                    - 조회 결과는 4개씩 반환하며, paymentId로 구분
                    1. 최초 조회 요청이면 paymentId는 0
                    2. 2번째 요청부터 paymentId는 직전 요청의 조회 결과 4개 중 마지막 paymentId""")
    })
    @GetMapping("/customers")
    public ResponseEntity<List<PaymentGetCustomerResponse>> getPaymentsByCustomer(@RequestParam String status,
                                                                                  @RequestParam Long paymentId,
                                                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomer(paymentId, status,
                customUserDetails.getCustomer().getCustomerId()));
    }
}
