package com.example.sharemind.chatMessage.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chatMessage.application.ChatMessageService;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.chatMessage.dto.response.ChatMessageGetResponse;
import com.example.sharemind.consult.exception.ConsultException;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ChatMessage Controller", description = "채팅 메세지(실시간) 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatMessages")
@RestController
public class ChatMessageController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Operation(summary = "채팅 메세지 조회", description = "채팅방에 해당하는 채팅메세지 리턴해주는 함수"
            + "- 주소 형식 : /api/v1/chatMessages/{chatId}?isCustomer=true&messageId=0"
            + "해당하는 메세지가 없을 경우 빈배열 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅 아이디로 요청됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionResponse.class))
            )
    })
    @Parameters({
            @Parameter(name = "isCustomer", description = "구매자이면 true"),
            @Parameter(name = "messageId", description = """
                    1. messageId를 기준으로 그보다 index가 작은 것에서 11개 리턴
                    2. 처음 요청을 할 때, 즉 messageId를 모를 때는 0으로 요청부탁드립니다.""")
    })
    @GetMapping("/{chatId}")
    public ResponseEntity<List<ChatMessageGetResponse>> getChatMessage(@PathVariable Long chatId,
                                                                       @RequestParam Long messageId,
                                                                       @RequestParam Boolean isCustomer,
                                                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(
                chatMessageService.getChatMessage(chatId, messageId, customUserDetails.getCustomer().getCustomerId(),
                        isCustomer));
    }

    @MessageMapping("/api/v1/chatMessages/customers/{chatId}")
    public ResponseEntity<Void> getCustomerChatMessage(@DestinationVariable Long chatId,
                                                       ChatMessageCreateRequest request,
                                                       SimpMessageHeaderAccessor headerAccessor) {
        try {
            handleChatMessage(chatId, request, headerAccessor, true);
        } catch (ChatException | ConsultException e) {
            simpMessagingTemplate.convertAndSend("/queue/chattings/exception/counselors/" + chatId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/api/v1/chatMessages/counselors/{chatId}")
    public ResponseEntity<Void> getCounselorsChatMessage(@DestinationVariable Long chatId,
                                                         ChatMessageCreateRequest request,
                                                         SimpMessageHeaderAccessor headerAccessor) {
        try {
            handleChatMessage(chatId, request, headerAccessor, false);
        } catch (ChatException | ConsultException e) {
            simpMessagingTemplate.convertAndSend("/queue/chattings/exception/counselors/" + chatId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    private void handleChatMessage(Long chatId, ChatMessageCreateRequest request,
                                   SimpMessageHeaderAccessor headerAccessor, boolean isCustomer) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());

        chatService.validateChatWithWebSocket(chatId, sessionAttributes, isCustomer);

        String nickName = (String) sessionAttributes.get("userNickname");
        chatMessageService.createAndSendChatMessage(request, chatId, isCustomer, nickName);
    }
}
