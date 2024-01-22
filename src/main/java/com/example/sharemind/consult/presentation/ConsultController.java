package com.example.sharemind.consult.presentation;

import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.dto.request.ConsultCreateRequest;
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
}
