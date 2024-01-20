package com.example.sharemind.admin.presentation;

import com.example.sharemind.admin.application.AdminService;
import com.example.sharemind.admin.dto.response.ConsultsGetUnpaidResponse;
import com.example.sharemind.admin.dto.response.CounselorGetPendingResponse;
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

    @Operation(summary = "미결제 상담 리스트 조회", description = "결제 여부(isPaid)가 false인 consult 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/unpaid-consults")
    public ResponseEntity<List<ConsultsGetUnpaidResponse>> getUnpaidConsults() {
        return ResponseEntity.ok(adminService.getUnpaidConsults());
    }

    @Operation(summary = "상담 결제 여부 수정", description = "결제 여부(isPaid)가 false인 consult를 true로 수정")
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
    public ResponseEntity<Void> updateIsPaid(@PathVariable Long consultId) {
        adminService.updateIsPaid(consultId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "심사 대기 중인 상담사 프로필 조회", description = "심사 대기 중인 상담사 프로필 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/pending-profiles")
    public ResponseEntity<List<CounselorGetPendingResponse>> getPendingCounselors() {
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
}
