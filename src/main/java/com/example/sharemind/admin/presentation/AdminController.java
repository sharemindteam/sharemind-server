package com.example.sharemind.admin.presentation;

import com.example.sharemind.admin.application.AdminService;
import com.example.sharemind.admin.dto.response.ConsultGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.CounselorGetByNicknameOrEmailResponse;
import com.example.sharemind.admin.dto.response.CustomerGetByNicknameOrEmailResponse;
import com.example.sharemind.admin.dto.response.InformationGetResponse;
import com.example.sharemind.admin.dto.response.PaymentGetRefundWaitingResponse;
import com.example.sharemind.admin.dto.response.PaymentGetSettlementOngoingResponse;
import com.example.sharemind.admin.dto.response.PostGetByIdResponse;
import com.example.sharemind.admin.dto.response.PostGetUnpaidPrivateResponse;
import com.example.sharemind.counselor.dto.response.CounselorGetProfileResponse;
import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.sms.application.SmsService;
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
import reactor.core.publisher.Mono;

@Tag(name = "Admin Controller", description = "관리자 페이지 컨트롤러")
@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final SmsService smsService;

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
    public ResponseEntity<Void> updateProfileStatus(@PathVariable Long counselorId,
            @RequestParam Boolean isPassed) {
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

    @Operation(summary = "닉네임, 이메일로 사용자 조회", description = "닉네임, 이메일로 사용자 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @Parameters({
            @Parameter(name = "keyword", description = "조회할 사용자의 닉네임 또는 이메일")
    })
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerGetByNicknameOrEmailResponse>> getCustomersByNicknameOrEmail(
            @RequestParam String keyword) {
        return ResponseEntity.ok(adminService.getCustomersByNicknameOrEmail(keyword));
    }

    @Operation(summary = "특정 사용자 로그인 제재 여부 수정", description = "특정 사용자 로그인 제재 여부 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 아이디로 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "customerId", description = "사용자 아이디"),
            @Parameter(name = "isBanned", description = "제재 설정 시 true, 제재 해제 시 false")
    })
    @PatchMapping("/customers/{customerId}")
    public ResponseEntity<Void> updateCustomerIsBanned(@PathVariable Long customerId,
            @RequestParam Boolean isBanned) {
        adminService.updateCustomerIsBanned(customerId, isBanned);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "닉네임, 이메일로 상담사 조회", description = "닉네임, 이메일로 상담사 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @Parameters({
            @Parameter(name = "keyword", description = "조회할 상담사의 닉네임 또는 이메일")
    })
    @GetMapping("/counselors")
    public ResponseEntity<List<CounselorGetByNicknameOrEmailResponse>> getCounselorsByNicknameOrEmail(
            @RequestParam String keyword) {
        return ResponseEntity.ok(adminService.getCounselorsByNicknameOrEmail(keyword));
    }

    @Operation(summary = "특정 상담사 프로필 대기 상태로 수정", description = "특정 상담사 프로필 대기 상태로 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상담사 아이디로 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "counselorId", description = "상담사 아이디")
    })
    @PatchMapping("/counselors/{counselorId}")
    public ResponseEntity<Void> updateCounselorPending(@PathVariable Long counselorId) {
        adminService.updateCounselorPending(counselorId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "아이디로 공개상담 조회", description = "아이디로 공개상담 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공개상담 아이디로 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "조회할 공개상담 아이디")
    })
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostGetByIdResponse> getPostByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(adminService.getPostByPostId(postId));
    }

    @Operation(summary = "특정 공개상담 게시물 삭제", description = "특정 공개상담 게시물 비활성화 상태로 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공개상담 아이디로 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "postId", description = "삭제할 공개상담 아이디")
    })
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePostByPostId(@PathVariable Long postId) {
        adminService.deletePostByPostId(postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 관련 수치 정보 조회", description = "회원 관련 수치 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/informations")
    public ResponseEntity<InformationGetResponse> getInformation() {
        return ResponseEntity.ok(adminService.getInformation());
    }

    @Operation(summary = "서비스 셧다운 여부 수정", description = "서비스 셧다운 여부 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @Parameters({
            @Parameter(name = "shutdown", description = "true: 서비스 셧다운, false: 서비스 정상 동작")
    })
    @PatchMapping("/managements")
    public ResponseEntity<Boolean> updateShutdown(@RequestParam Boolean shutdown) {
        return ResponseEntity.ok(adminService.updateShutdown(shutdown));
    }

    @Operation(summary = "서비스 셧다운 여부 조회", description = "서비스 셧다운 여부 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @GetMapping("/managements")
    public ResponseEntity<Boolean> getShutdown() {
        return ResponseEntity.ok(adminService.getShutdown());
    }

    @Operation(summary = "문자 전송", description = "사용자에게 문자 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발송 성공")
    })
    @PostMapping("/sms")
    public ResponseEntity<String> sendSms() {
        return ResponseEntity.ok(smsService.sendSms("01026371757").block());
    }
}
