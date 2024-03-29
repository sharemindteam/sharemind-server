package com.example.sharemind.admin.presentation;

import com.example.sharemind.admin.application.AdminService;
import com.example.sharemind.admin.dto.response.ConsultGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.PaymentGetRefundWaitingResponse;
import com.example.sharemind.admin.dto.response.PaymentGetSettlementOngoingResponse;
import com.example.sharemind.admin.dto.response.PostGetUnpaidPrivateResponse;
import com.example.sharemind.counselor.dto.response.CounselorGetProfileResponse;
import com.example.sharemind.global.exception.CustomExceptionResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin Controller", description = "관리자 페이지 컨트롤러")
@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "미결제 상담(편지/채팅) 리스트 조회", description = "결제 여부(isPaid)가 false인 consult 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/unpaid-consults")
    public ResponseEntity<List<ConsultGetUnpaidResponse>> getUnpaidConsults() {
        return ResponseEntity.ok(adminService.getUnpaidConsults());
    }

    @Operation(summary = "상담(편지/채팅) 결제 여부 수정", description = "결제 여부(isPaid)가 false인 consult를 true로 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "이미 결제 완료된 상담",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상담 아이디로 요청됨",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "consultId", description = "상담 아이디")
    })
    @PatchMapping("/unpaid-consults/{consultId}")
    public ResponseEntity<Void> updateConsultIsPaid(@PathVariable Long consultId) {
        adminService.updateConsultIsPaid(consultId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "심사 대기 중인 상담사 프로필 조회", description = "심사 대기 중인 상담사 프로필 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/pending-profiles")
    public ResponseEntity<List<CounselorGetProfileResponse>> getPendingCounselors() {
        return ResponseEntity.ok(adminService.getPendingCounselors());
    }

    @Operation(summary = "상담사 프로필 심사 상태 수정",
            description = "상담사 프로필 심사 상태 수정(최초 심사 통과 시 COUNSELOR 권한 부여), " +
                    "주소 형식: /api/v1/admins/pending-profiles/{counselorId}?isPassed=true")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "심사 중이 아닌 상담사 프로필에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상담사 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "counselorId", description = "상담사 아이디"),
            @Parameter(name = "isPassed", description = "심사 통과 여부")
    })
    @PatchMapping("/pending-profiles/{counselorId}")
    public ResponseEntity<Void> updateProfileStatus(@PathVariable Long counselorId, @RequestParam Boolean isPassed) {
        adminService.updateProfileStatus(counselorId, isPassed);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "환불 예정 결제 정보 조회", description = "환불 예정 상태인 결제 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/refund-waiting")
    public ResponseEntity<List<PaymentGetRefundWaitingResponse>> getRefundWaitingPayments() {
        return ResponseEntity.ok(adminService.getRefundWaitingPayments());
    }

    @Operation(summary = "결제 환불 완료 수정",
            description = "환불 예정인 결제를 환불 완료로 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "환불 예정 상태가 아닌 결제에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 결제 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "paymentId", description = "결제 아이디")
    })
    @PatchMapping("/refund-waiting/{paymentId}")
    public ResponseEntity<Void> updateRefundComplete(@PathVariable Long paymentId) {
        adminService.updateRefundComplete(paymentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상담사 정산 요청 정보 조회", description = "상담사가 정산을 요청한 정산 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/settlement-ongoing")
    public ResponseEntity<List<PaymentGetSettlementOngoingResponse>> getSettlementOngoingPayments() {
        return ResponseEntity.ok(adminService.getSettlementOngoingPayments());
    }

    @Operation(summary = "정산 완료 수정",
            description = "정산 중인 정보를 정산 완료로 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "정산 중 상태가 아닌 정보에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 payment 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "paymentId", description = "payment 아이디")
    })
    @PatchMapping("/settlement-ongoing/{paymentId}")
    public ResponseEntity<Void> updateSettlementComplete(@PathVariable Long paymentId) {
        adminService.updateSettlementComplete(paymentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "미결제 일대다 상담 리스트 조회", description = "결제 여부(isPaid)가 false인 일대다 상담 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/unpaid-posts")
    public ResponseEntity<List<PostGetUnpaidPrivateResponse>> getUnpaidPrivatePosts() {
        return ResponseEntity.ok(adminService.getUnpaidPrivatePosts());
    }

    @Operation(summary = "일대다 비공개 상담 결제 여부 수정", description = "결제 여부(isPaid)가 false인 일대다 상담을 true로 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "이미 결제 완료된 상담",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일대다 상담 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "일대다 상담 아이디")
    })
    @PatchMapping("/unpaid-posts/{postId}")
    public ResponseEntity<Void> updatePostIsPaid(@PathVariable Long postId) {
        adminService.updatePostIsPaid(postId);
        return ResponseEntity.ok().build();
    }
}
