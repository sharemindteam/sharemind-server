package com.example.sharemind.chatMessage.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
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

    @MessageMapping("/chatMessage/customers/{chattingRoomId}")
    public void getCustomerChatMessage(@DestinationVariable Long chattingRoomId,
                                       ChatMessageCreateRequest chatMessageCreateRequest,
                                       SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());
        Long userId = null;
        String userNickname = null;
        if (sessionAttributes.containsKey("userId")) {
            userId = (Long) sessionAttributes.get("userId");
        }
        //todo: userId가 해당 방에 속해있는지

        //todo: userId가 customer가 맞는지
        //todo: chatMessage 저장 후 전송
    }
}
