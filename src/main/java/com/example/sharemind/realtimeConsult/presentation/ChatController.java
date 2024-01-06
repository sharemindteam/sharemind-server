package com.example.sharemind.realtimeConsult.presentation;

import com.example.sharemind.realtimeConsult.application.RealtimeConsultService;
import com.example.sharemind.realtimeConsult.dto.request.ChattingRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RealtimeConsultService realtimeConsultService;

    @MessageMapping("/chattings/{chattingRoomId}/messages")
    public void chat(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest) {
        simpMessagingTemplate.convertAndSend("/queue/chattings/" + chattingRoomId, chattingRequest);
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(),
                chattingRequest.getSenderName(), chattingRoomId);
    }

    @GetMapping("/channels") //todo: 채팅 데모용을 위한 api 삭제해야함
    public ResponseEntity<List<Long>> getChannelList(@RequestParam Long customerId) {
        return ResponseEntity.ok(realtimeConsultService.getRealtimeConsult(customerId));
    }
}
