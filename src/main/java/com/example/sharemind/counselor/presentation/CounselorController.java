package com.example.sharemind.counselor.presentation;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.dto.request.CounselorUpdateProfileRequest;
import com.example.sharemind.counselor.dto.response.CounselorGetInfoResponse;
import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "이미 퀴즈 통과한 상담사에 대해 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
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
            @ApiResponse(responseCode = "200", description = "조회 성공(이미 퀴즈 통과한 경우도 false 반환)"),
            @ApiResponse(responseCode = "404", description = "상담사 정보 존재하지 않음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @GetMapping("/quiz")
    public ResponseEntity<Boolean> getRetryPermission(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(counselorService.getRetryPermission(customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "프로필 편집",
            description = "상담사 프로필 수정 신청(consultTimes, letterCost, chatCost 제외 null 불가)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 신청 성공"),
            @ApiResponse(responseCode = "400", description = """
                    1. request 값 잘못 들어감(ex. 닉네임이 공백, 한줄 소개가 50자 이상)
                    2. 이미 프로필 심사 중인 상담사에 대한 요청
                    3. 한 요일에 대한 상담 가능 시간이 2개 초과""",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "교육을 수료하지 않은 상담사에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = """
                    1. 상담사 정보 존재하지 않음
                     2. 올바르지 않은 상담 카테고리/스타일/방식/요일
                     3. 선택한 상담 방식에 대한 상담료 입력되지 않음""",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "한 요일에 대한 상담 가능 시간이 서로 겹침(ex. 13~15, 14~20)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PatchMapping("/profiles")
    public ResponseEntity<Void> updateCounselorProfile(@Valid @RequestBody CounselorUpdateProfileRequest counselorUpdateProfileRequest,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        counselorService.updateCounselorProfile(counselorUpdateProfileRequest, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상담사 내 정보 조회", description = "내 정보 페이지에 필요한 상담사 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보 존재하지 않음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @GetMapping("/my-info")
    public ResponseEntity<CounselorGetInfoResponse> getCounselorMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(counselorService.getCounselorMyInfo(customUserDetails.getCustomer().getCustomerId()));
    }
}
