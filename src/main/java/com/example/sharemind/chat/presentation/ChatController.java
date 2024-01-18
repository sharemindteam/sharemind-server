package com.example.sharemind.chat.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.dto.request.ChatStatusUpdateRequest;
import com.example.sharemind.chat.dto.response.ChatInfoGetResponse;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat Controller", description = "채팅(실시간) 컨트롤러")
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Operation(summary = "채팅 목록 반환", description = "속해 있는 채팅 목록을 전부 가져오는 api, 메세지 정렬 읽지 않은 순, 완료/취소된 상담 포함이 아직 구현되지 않아, 개선이 되어야하는 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅 목록 반환 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상담사 아이디로 요청됨")
    })
    @GetMapping()
    public ResponseEntity<List<ChatInfoGetResponse>> getChatList(@RequestParam Boolean isCustomer,
                                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ChatInfoGetResponse> chatInfoGetResponses = chatService.getChatInfoByCustomerId(
                customUserDetails.getCustomer().getCustomerId(), isCustomer);
        return ResponseEntity.ok(chatInfoGetResponses);
        //todo: 메세지 읽지 않은 순, 완료/취소된 상담 포함한 것도 구현해야함ㅜㅜ
    }

    @MessageMapping("/api/v1/chat/customers/{chatId}")
    public ResponseEntity<Void> getAndSendCustomerChatStatus(@DestinationVariable Long chatId,
                                                             ChatStatusUpdateRequest chatStatusUpdateRequest,
                                                             SimpMessageHeaderAccessor headerAccessor) {
        try {
            Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());

            chatService.validateChat(chatId, sessionAttributes, true);

            chatService.getAndSendChatStatus(chatId, chatStatusUpdateRequest, true);
        } catch (ChatException | ConsultException e) {
            simpMessagingTemplate.convertAndSend("/queue/chattings/exception/customers/" + chatId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/api/v1/chat/counselors/{chatId}")
    public ResponseEntity<Void> getAndSendCounselorChatStatus(@DestinationVariable Long chatId,
                                                              ChatStatusUpdateRequest chatStatusUpdateRequest,
                                                              SimpMessageHeaderAccessor headerAccessor) {
        try {
            Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());

            chatService.validateChat(chatId, sessionAttributes, false);

            chatService.getAndSendChatStatus(chatId, chatStatusUpdateRequest, false);
        } catch (ChatException | ConsultException e) {
            simpMessagingTemplate.convertAndSend("/queue/chattings/exception/counselors/" + chatId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
}
