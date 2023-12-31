package com.example.sharemind.realtimeConsult.presentation;

import com.example.sharemind.realtimeConsult.application.RealtimeConsultService;
import com.example.sharemind.realtimeConsult.dto.request.ChattingRequest;
import com.example.sharemind.realtimeConsult.dto.response.ChattingResponse;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RealtimeChattingController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RealtimeConsultService realtimeConsultService;

    @MessageMapping("/chattings/customers/{chattingRoomId}")
    public void chatCustomer(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest,
                             SimpMessageHeaderAccessor headerAccessor) {
        String customerNickname = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes())
                .get("userNickname");
        ChattingResponse chattingResponse = ChattingResponse.of(chattingRequest.getContent(), customerNickname);
        simpMessagingTemplate.convertAndSend("/queue/chattings/customers/" + chattingRoomId, chattingResponse);
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(),
                customerNickname, chattingRoomId);
    }

    @MessageMapping("/chattings/counselors/{chattingRoomId}")
    public void chatCounselor(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest,
                              SimpMessageHeaderAccessor headerAccessor) {
        String counselorNickname = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes())
                .get("userNickname");
        ChattingResponse chattingResponse = ChattingResponse.of(chattingRequest.getContent(), counselorNickname);
        simpMessagingTemplate.convertAndSend("/queue/chattings/counselors/" + chattingRoomId, chattingResponse);
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(),
                counselorNickname, chattingRoomId);
    }

    @GetMapping("/channels") //todo: 채팅 데모용을 위한 api 삭제해야함
    public ResponseEntity<List<Long>> getChannelList(@RequestParam Long userId, @RequestParam Boolean isCustomer) {
        return ResponseEntity.ok(realtimeConsultService.getRealtimeConsult(userId, isCustomer));
    }
}
