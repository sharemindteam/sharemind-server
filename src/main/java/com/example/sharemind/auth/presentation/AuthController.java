package com.example.sharemind.auth.presentation;

import com.example.sharemind.auth.application.AuthService;
import com.example.sharemind.auth.dto.request.*;
import com.example.sharemind.auth.dto.response.TokenDto;
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

@Tag(name = "Auth Controller", description = "인증 컨트롤러")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "customer 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "올바르지 않은 이메일/비밀번호 형식",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일 주소",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@Valid @RequestBody AuthSignUpRequest authSignUpRequest) {
        authService.signUp(authSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인", description = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호 불일치",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이메일로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping("/signIn")
    public ResponseEntity<TokenDto> signIn(
            @Valid @RequestBody AuthSignInRequest authSignInRequest) {
        return ResponseEntity.ok(authService.signIn(authSignInRequest));
    }

    @Operation(summary = "accessToken, refreshToken 재발급",
            description = "accessToken 유효기간 1시간, refreshToken 유효기간 1주 (refreshToken도 같이 재발급되므로 1주가 지나지 않아도 재발급 후엔 이전 refreshToken은 사용 불가)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 refreshToken",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원에 대해 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissueToken(
            @Valid @RequestBody AuthReissueRequest authReissueRequest) {
        return ResponseEntity.ok(authService.reissueToken(authReissueRequest));
    }

    @Operation(summary = "비밀번호 변경 시 현재 비밀번호 일치 여부 조회",
            description = "비밀번호 변경 시 현재 비밀번호 일치 여부 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호 값이 공백으로 들어옴",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping("/password")
    public ResponseEntity<Boolean> getPasswordMatched(
            @Valid @RequestBody AuthGetPasswordMatchRequest authGetPasswordMatchRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(authService.getPasswordMatched(authGetPasswordMatchRequest,
                customUserDetails.getCustomer().getCustomerId()));
    }

    @Operation(summary = "비밀번호 변경",
            description = "비밀번호 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경 성공"),
            @ApiResponse(responseCode = "400", description = "1. 올바르지 않은 비밀번호 형식\n 2. 새 비밀번호가 현재 비밀번호와 동일",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody AuthUpdatePasswordRequest authUpdatePasswordRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        authService.updatePassword(authUpdatePasswordRequest,
                customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴",
            description = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 미완료 상담이 남아있음\n 2. 환불금/정산금 남아있음\n 3. 탈퇴 사유(shortReason) 공백으로 들어옴",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @DeleteMapping("/quit")
    public ResponseEntity<Void> quit(@Valid @RequestBody AuthQuitRequest authQuitRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        authService.quit(authQuitRequest, customUserDetails.getCustomer().getCustomerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃",
            description = "로그아웃")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    @PatchMapping("/signOut")
    public ResponseEntity<Void> signOut(@Valid @RequestBody AuthSignOutRequest authSignOutRequest) {
        authService.signOut(authSignOutRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "아이디 찾기",
            description = "복구 이메일로 아이디 찾기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "복구 이메일로 아이디 전송 성공"),
            @ApiResponse(responseCode = "400", description = "1. 올바르지 않은 복구 이메일 형식",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 복구 이메일을 가진 유저가 존재하지 않음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PatchMapping("/find-id")
    public ResponseEntity<Void> findIdByRecoveryEmail(
            @Valid @RequestBody AuthFindIdRequest authFindIdRequest) {
        authService.sendIdByRecoveryEmail(authFindIdRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호 찾기",
            description = "복구 이메일로 비밀번호 찾기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "복구 이메일로 새로운 비밀번호 전송 성공"),
            @ApiResponse(responseCode = "400", description = "1. 올바르지 않은 복구 이메일 형식",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 복구 이메일을 가진 유저가 존재하지 않음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PatchMapping("/find-password")
    public ResponseEntity<Void> findPasswordByRecoveryEmail(
            @Valid @RequestBody AuthFindPasswordRequest authFindPasswordRequest) {
        authService.updateAndSendPasswordByRecoveryEmail(authFindPasswordRequest);
        return ResponseEntity.ok().build();
    }
}
