package com.example.sharemind.chatMessage.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.dto.request.ChattingRequest;
import com.example.sharemind.chat.dto.response.ChattingResponse;
import com.example.sharemind.chatMessage.application.ChatMessageService;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.chatMessage.dto.response.ChatMessageResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chatMessage/customers/{chatId}")
    public void getCustomerChatMessage(@DestinationVariable Long chatId,
                                       ChatMessageCreateRequest chatMessageCreateRequest,
                                       SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());

        chatService.validateChat(sessionAttributes, chatId);

        chatMessageService.createChatMessage(chatMessageCreateRequest, chatId, true);

        String customerNickname = (String) sessionAttributes.get("userNickname");
        ChatMessageResponse chatMessageResponse = ChatMessageResponse.of(customerNickname,
                chatMessageCreateRequest.getContent(), true);

        simpMessagingTemplate.convertAndSend("/queue/chattings/counselors/" + chatId, chatMessageResponse);
        simpMessagingTemplate.convertAndSend("/queue/chattings/customers/" + chatId, chatMessageResponse);
        log.info("Message [{}] send by member: {} to chatting room: {}", chatMessageCreateRequest.getContent(),
                customerNickname, chatId);
    }
}
