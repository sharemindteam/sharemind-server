package com.example.sharemind.auth.presentation;

import com.example.sharemind.auth.application.AuthService;
import com.example.sharemind.auth.dto.request.AuthReissueRequest;
import com.example.sharemind.auth.dto.request.AuthSignInRequest;
import com.example.sharemind.auth.dto.request.AuthSignUpRequest;
import com.example.sharemind.auth.dto.response.TokenDto;
import com.example.sharemind.global.exception.CustomExceptionResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Controller", description = "인증 컨트롤러")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "customer 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "1. 이미 가입된 이메일 주소\n 2. 올바르지 않은 이메일/비밀번호/전화번호 형식\n 3. 로그인 이메일과 복구 이메일 주소가 동일",
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
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody AuthSignInRequest authSignInRequest) {
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
    public ResponseEntity<TokenDto> reissueToken(@Valid @RequestBody AuthReissueRequest authReissueRequest) {
        return ResponseEntity.ok(authService.reissueToken(authReissueRequest));
    }
}
