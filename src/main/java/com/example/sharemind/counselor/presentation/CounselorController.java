package com.example.sharemind.counselor.presentation;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Counselor Controller", description = "상담사 컨트롤러")
@RestController
@RequestMapping("/api/v1/counselors")
@RequiredArgsConstructor
public class CounselorController {

    private final CounselorService counselorService;

    @Operation(summary = "퀴즈 통과 여부 수정",
            description = "상담사 인증 퀴즈 통과 여부 수정, 첫 퀴즈 응시일 경우 상담사 데이터 생성, 주소 형식: /api/v1/counselors/quiz?isEducated=true")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @Parameters({
            @Parameter(name = "isEducated", description = "퀴즈 통과 여부")
    })
    @PostMapping("/quiz")
    public ResponseEntity<Void> updateIsEducated(@RequestParam Boolean isEducated,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        counselorService.updateIsEducated(isEducated, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "퀴즈 재응시 가능 여부 조회", description = "퀴즈 통과 실패 후 24시간 경과 여부 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @GetMapping("/quiz")
    public ResponseEntity<Boolean> getRetryPermission(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(counselorService.getRetryPermission(customUserDetails.getCustomer().getCustomerId()));
    }
}
