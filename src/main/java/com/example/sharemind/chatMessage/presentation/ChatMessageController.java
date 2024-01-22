package com.example.sharemind.chatMessage.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chatMessage.application.ChatMessageService;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.consult.exception.ConsultException;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ChatMessage Controller", description = "채팅 메세지(실시간) 컨트롤러")
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

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
