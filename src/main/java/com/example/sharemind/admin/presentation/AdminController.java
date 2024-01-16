package com.example.sharemind.admin.presentation;

import com.example.sharemind.admin.application.AdminService;
import com.example.sharemind.admin.dto.response.ConsultsGetUnpaidResponse;
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

@CrossOrigin
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
    @GetMapping
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
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 상담 아이디로 요청됨\n 2. 존재하지 않는 후원 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "consultId", description = "상담 아이디")
    })
    @PatchMapping("/{consultId}")
    public ResponseEntity<Void> updateIsPaid(@PathVariable Long consultId) {
        adminService.updateIsPaid(consultId);
        return ResponseEntity.ok().build();
    }
}
