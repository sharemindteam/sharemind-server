package com.example.sharemind.consult.presentation;

import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.dto.request.ConsultCreateRequest;
import com.example.sharemind.consult.dto.response.ConsultGetOngoingResponse;
import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.sharemind.global.constants.Constants.IS_COUNSELOR;
import static com.example.sharemind.global.constants.Constants.IS_CUSTOMER;

@Tag(name = "Consult Controller", description = "상담 컨트롤러")
@RestController
@RequestMapping("/api/v1/consults")
@RequiredArgsConstructor
public class ConsultController {

    private final ConsultService consultService;

    @Operation(summary = "상담 신청", description = "consult 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신청 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 프로필 심사가 완료되지 않은 상담사 아이디로 요청됨\n 2. 상담사가 제공하지 않는 상담 유형",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "1. 존재하지 않는 상담사 아이디로 요청됨\n 2. 존재하지 않는 상담 종류로 요청됨\n 3. 상담 종류에 대한 상담료 존재하지 않음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Void> createConsult(@Valid @RequestBody ConsultCreateRequest consultCreateRequest,
                                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        consultService.createConsult(consultCreateRequest, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "상담사 진행 중인 상담 조회", description = "상담사 진행 중인 최근 상담 3개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/counselors")
    public ResponseEntity<ConsultGetOngoingResponse> getOngoingConsultsByCounselor(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(consultService.getOngoingConsults(customUserDetails.getCustomer().getCustomerId(),
                IS_COUNSELOR));
    }

    @Operation(summary = "구매자 진행 중인 상담 조회", description = "구매자 진행 중인 최근 상담 1개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/customers")
    public ResponseEntity<ConsultGetOngoingResponse> getOngoingConsultsByCustomer(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(consultService.getOngoingConsults(customUserDetails.getCustomer().getCustomerId(),
                IS_CUSTOMER));
    }
}
