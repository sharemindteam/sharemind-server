package com.example.sharemind.chat.presentation;

import com.example.sharemind.chat.dto.request.ChattingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chattings/{chattingRoomId}/messages")
    public void chat(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest) {
        simpMessagingTemplate.convertAndSend("/queue/chattings/" + chattingRoomId, chattingRequest);
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(),
                chattingRequest.getSenderName(), chattingRoomId);
    }
}
