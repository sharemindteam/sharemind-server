package com.example.sharemind.customer.presentation;

import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Controller", description = "구매자 컨트롤러")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "구매자 닉네임 조회", description = "구매자 내 정보 페이지에 필요한 닉네임 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보 존재하지 않음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @GetMapping("/nickname")
    public ResponseEntity<String> getCustomerNickname(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(customerService.getCustomerNickname(customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "customerId 조회", description = "채팅 연결에 필요한 customerId 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping()
    public ResponseEntity<Long> getCustomerId(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(customUserDetails.getCustomer().getCustomerId());
    }
}
