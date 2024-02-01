package com.example.sharemind.payment.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.payment.application.PaymentService;
import com.example.sharemind.payment.dto.response.PaymentGetCounselorHomeResponse;
import com.example.sharemind.payment.dto.response.PaymentGetCounselorResponse;
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
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "구매자 상담 취소 신청", description = "구매자가 상담 대기인 결제 내역 중 상담 취소 신청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "취소 성공"),
            @ApiResponse(responseCode = "400", description = "결제 완료 상태가 아닌 결제에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "취소 권한이 없는 결제에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 결제 정보",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "paymentId", description = "취소할 결제 정보 아이디")
    })
    @PatchMapping("/customers/{paymentId}")
    public ResponseEntity<Void> updateRefundWaitingByCustomer(@PathVariable Long paymentId,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        paymentService.updateRefundWaitingByCustomer(paymentId, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상담사 수익 관리 조회", description = "- 상담사 수익 관리 조회\n " +
            "- 주소 형식: /api/v1/payments/counselors?status=settle_complete&sort=all&paymentId=0")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(없으면 빈 배열 반환)"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 status\n 2. 존재하지 않는 sort",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "status",
                    description = "SETTLEMENT_WAITING(정산 예정), SETTLEMENT_ONGOING(정산 중), SETTLEMENT_COMPLETE(정산 완료)"),
            @Parameter(name = "sort",
                    description = "WEEK(최근 일주일), MONTH(최근 1개월), ALL(전체)"),
            @Parameter(name = "paymentId", description = """
                    - 조회 결과는 3개씩 반환하며, paymentId로 구분
                    1. 최초 조회 요청이면 paymentId는 0
                    2. 2번째 요청부터 paymentId는 직전 요청의 조회 결과 3개 중 마지막 paymentId""")
    })
    @GetMapping("/counselors")
    public ResponseEntity<List<PaymentGetCounselorResponse>> getPaymentsByCounselor(@RequestParam String status,
                                                                                    @RequestParam String sort,
                                                                                    @RequestParam Long paymentId,
                                                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(paymentService.getPaymentsByCounselor(
                paymentId, status, sort, customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "상담사 정산 신청", description = "상담사 정산 신청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신청 성공"),
            @ApiResponse(responseCode = "400", description = "정산 예정 상태가 아닌 정산에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "신청 권한이 없는 정산에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 정산 정보",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "paymentId", description = "신청할 정산 정보 아이디")
    })
    @PatchMapping("/counselors/{paymentId}")
    public ResponseEntity<Void> updateSettlementOngoingByCounselor(@PathVariable Long paymentId,
                                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        paymentService.updateSettlementOngoingByCounselor(paymentId, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상담사 홈화면 수익 관리 조회", description = "상담사 홈화면 수익 관리 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/counselors/home")
    public ResponseEntity<PaymentGetCounselorHomeResponse> getPaymentInfoForCounselorHome(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(paymentService.getCounselorHomePayment(customUserDetails.getCustomer().getCustomerId()));
    }
}
