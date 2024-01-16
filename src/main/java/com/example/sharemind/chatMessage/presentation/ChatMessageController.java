package com.example.sharemind.chatMessage.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chatMessage.application.ChatMessageService;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ChatMessage Controller", description = "채팅 메세지(실시간) 컨트롤러")
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/api/v1/chatMessages/customers/{chatId}")
    public void getCustomerChatMessage(@DestinationVariable Long chatId,
                                       ChatMessageCreateRequest request,
                                       SimpMessageHeaderAccessor headerAccessor) {
        handleChatMessage(chatId, request, headerAccessor, true);
    }

    @MessageMapping("/api/v1/chatMessages/counselors/{chatId}")
    public void getCounselorsChatMessage(@DestinationVariable Long chatId,
                                         ChatMessageCreateRequest request,
                                         SimpMessageHeaderAccessor headerAccessor) {
        handleChatMessage(chatId, request, headerAccessor, false);
    }

    private void handleChatMessage(Long chatId, ChatMessageCreateRequest request,
                                   SimpMessageHeaderAccessor headerAccessor, boolean isCustomer) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());

        chatService.validateChat(chatId, sessionAttributes, isCustomer);

        String nickName = (String) sessionAttributes.get("userNickname");
        chatMessageService.createAndSendChatMessage(request, chatId, isCustomer, nickName);
    }
}
