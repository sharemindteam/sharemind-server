package com.example.sharemind.letterMessage.presentation;

import com.example.sharemind.global.exception.CustomExceptionResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.letterMessage.application.LetterMessageService;
import com.example.sharemind.letterMessage.dto.request.*;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetIsSavedResponse;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetRecentTypeResponse;
import com.example.sharemind.letterMessage.dto.response.LetterMessageGetResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LetterMessage Controller", description = "편지(비실시간 상담) 메시지 컨트롤러")
@RestController
@RequestMapping("/api/v1/letterMessages")
@RequiredArgsConstructor
public class LetterMessageController {

    private final LetterMessageService letterMessageService;

    @Operation(summary = "메시지 최초 생성", description = "임시저장 메시지가 없는 메시지 유형에 대한 최초 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 이미 최초 생성된 메시지 유형에 대한 요청\n 2. 올바른 순서의 메시지 유형이 아님(ex. 첫번째 답장 순서에 추가 질문 생성 요청)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403",
                    description = "작성 권한이 없는 메시지에 대한 요청(ex. 해당 상담의 구매자/판매자가 아님, 구매자가 질문이 아닌 답장 쓰는 것으로 요청)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 편지 아이디로 요청됨\n 2. 존재하지 않는 메시지 유형으로 요청됨\n",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Void> createLetterMessage(@Valid @RequestBody LetterMessageCreateRequest letterMessageCreateRequest,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.createLetterMessage(letterMessageCreateRequest, customUserDetails.getCustomer());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "메시지 수정", description = "임시저장 메시지가 있는 메시지 유형에 대한 메시지 내용 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400",
                    description = "이미 최종 제출된 메시지 유형에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403",
                    description = "작성 권한이 없는 메시지에 대한 요청(ex. 해당 메시지의 작성자가 아님)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 메시지 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PatchMapping
    public ResponseEntity<Void> updateLetterMessage(@Valid @RequestBody LetterMessageUpdateRequest letterMessageUpdateRequest,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.updateLetterMessage(letterMessageUpdateRequest, customUserDetails.getCustomer());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "첫번째 질문 메시지 최초 생성", description = "임시저장 메시지가 없는 첫번째 질문에 대한 최초 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400",
                    description = "1. 이미 최초 생성된 메시지 유형에 대한 요청\n 2. 올바른 순서의 메시지 유형이 아님(ex. 첫번째 답장 순서에 추가 질문 생성 요청)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403",
                    description = "작성 권한이 없는 메시지에 대한 요청(ex. 해당 상담의 구매자/판매자가 아님, 구매자가 질문이 아닌 답장 쓰는 것으로 요청)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 편지 아이디로 요청됨\n 2. 존재하지 않는 상담 카테고리로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PostMapping("/first-question")
    public ResponseEntity<Void> createFirstQuestion(@Valid @RequestBody LetterMessageCreateFirstRequest letterMessageCreateFirstRequest,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.createFirstQuestion(letterMessageCreateFirstRequest, customUserDetails.getCustomer());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "첫번째 질문 수정", description = "임시저장 메시지가 있는 첫번째 질문에 대한 메시지 내용, 상담 카테고리 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400",
                    description = "이미 최종 제출된 메시지 유형에 대한 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "403",
                    description = "작성 권한이 없는 메시지에 대한 요청(ex. 해당 메시지의 작성자가 아님)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 메시지 아이디로 요청됨\n 2. 존재하지 않는 상담 카테고리로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @PatchMapping("/first-question")
    public ResponseEntity<Void> updateFirstQuestion(@Valid @RequestBody LetterMessageUpdateFirstRequest letterMessageUpdateFirstRequest,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        letterMessageService.updateFirstQuestion(letterMessageUpdateFirstRequest, customUserDetails.getCustomer());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "임시저장 메시지 존재 여부 조회",
            description = "특정 편지의 특정 메시지 유형에 대한 임시저장 메시지 존재 여부 조회, 주소 형식: /api/v1/letterMessages/drafts/{letterId}?messageType=first_reply")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(임시저장 메시지 존재하지 않으면 수정일시는 null로 반환됨)"),
            @ApiResponse(responseCode = "403",
                    description = "접근 권한이 없는 메시지에 대한 요청(ex. 해당 상담의 참여자가 아님)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 편지 아이디로 요청됨\n 2. 존재하지 않는 메시지 유형으로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "letterId", description = "편지 아이디"),
            @Parameter(name = "messageType", description = "메시지 유형")
    })
    @GetMapping("/drafts/{letterId}")
    public ResponseEntity<LetterMessageGetIsSavedResponse> getIsSaved(@PathVariable Long letterId, @RequestParam String messageType) {
        return ResponseEntity.ok(letterMessageService.getIsSaved(letterId, messageType));
    }

    @Operation(summary = "메시지 조회",
            description = "메시지 조회, 주소 형식: /api/v1/letterMessages/{letterId}?messageType=first_reply&isCompleted=true")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "1. 조회 성공\n 2. 올바른 편지 아이디와 메시지 유형으로 요청되었으나 해당하는 메시지가 아직 작성되지 않음(필드값이 모두 null로 반환됨)"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 편지 아이디로 요청됨\n 2. 존재하지 않는 메시지 유형으로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "letterId", description = "편지 아이디"),
            @Parameter(name = "messageType", description = "메시지 유형"),
            @Parameter(name = "isCompleted", description = "조회하려는 메시지가 임시저장된건지 최종 제출된건지")
    })
    @GetMapping("/{letterId}")
    public ResponseEntity<LetterMessageGetResponse> getLetterMessage(@PathVariable Long letterId,
                                                                     @RequestParam String messageType, @RequestParam Boolean isCompleted,
                                                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(letterMessageService.getLetterMessage(letterId, messageType, isCompleted, customUserDetails.getCustomer()));
    }

    @Operation(summary = "편지 진행 상태 조회",
            description = "편지에서 가장 최근에 작성된 메시지 종류 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공(아직 아무 메시지도 작성되지 않은 경우도 포함)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 편지 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "letterId", description = "편지 아이디")
    })
    @GetMapping("/recent-type/{letterId}")
    public ResponseEntity<LetterMessageGetRecentTypeResponse> getRecentMessageType(@PathVariable Long letterId) {
        return ResponseEntity.ok(letterMessageService.getRecentMessageType(letterId));
    }
}
