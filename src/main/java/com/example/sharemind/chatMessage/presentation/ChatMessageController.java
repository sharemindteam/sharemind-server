package com.example.sharemind.chatMessage.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chatMessage.application.ChatMessageService;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.chatMessage.dto.response.ChatMessageGetResponse;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.global.jwt.CustomUserDetails;
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
