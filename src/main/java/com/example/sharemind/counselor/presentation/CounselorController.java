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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
