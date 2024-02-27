package com.example.sharemind.chat.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.dto.request.ChatStatusUpdateRequest;
import com.example.sharemind.chat.dto.response.ChatGetRoomInfoResponse;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.*;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat Controller", description = "채팅(실시간) 컨트롤러")
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Operation(summary = "상담사 채팅 목록 반환", description = "상담사가 속해 있는 채팅 목록을 전부 가져오는 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅 목록 반환 성공"),
            @ApiResponse(responseCode = "404", description = "1. 상담사 role을 가지지 않은 사람이 이 api에 접근할 때"
                    + "2. sortType이 존재하지 않을 때")
    })
    @Parameters({
            @Parameter(name = "filter", description = "완료/취소된 상담 제외 여부"),
            @Parameter(name = "sortType", description = "정렬 방식(LATEST, UNREAD)")
    })
    @GetMapping("/counselors")
    public ResponseEntity<List<ChatLetterGetResponse>> getCounselorChatList(
            @RequestParam Boolean filter,
            @RequestParam String sortType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ChatLetterGetResponse> chatInfoGetResponses = chatService.getChatsInfoByCustomerId(
                customUserDetails.getCustomer().getCustomerId(), false, filter, sortType);
        return ResponseEntity.ok(chatInfoGetResponses);
    }

    @Operation(summary = "구매자 채팅 목록 반환", description = "구매자가 속해 있는 채팅 목록을 전부 가져오는 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅 목록 반환 성공"),
            @ApiResponse(responseCode = "404", description = "1. 구매자 role을 가지지 않은 사람이 해당 api를 요청할 때"
                    + "2. sortType이 존재하지 않을 때")
    })
    @Parameters({
            @Parameter(name = "filter", description = "완료/취소된 상담 제외 여부"),
            @Parameter(name = "sortType", description = "정렬 방식(LATEST, UNREAD)")
    })
    @GetMapping("/customers")
    public ResponseEntity<List<ChatLetterGetResponse>> getCustomerChatList(
            @RequestParam Boolean filter,
            @RequestParam String sortType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ChatLetterGetResponse> chatInfoGetResponses = chatService.getChatsInfoByCustomerId(
                customUserDetails.getCustomer().getCustomerId(), true, filter, sortType);
        return ResponseEntity.ok(chatInfoGetResponses);
    }

    @GetMapping("/counselors/{chatId}")
    public ResponseEntity<ChatGetRoomInfoResponse> getCounselorChatInfo(
            @PathVariable Long chatId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(chatService.getChatInfoByCounselor(chatId, customUserDetails.getCustomer().getCustomerId()));
    }

    @MessageMapping("/api/v1/chat/customers/{chatId}")
    public ResponseEntity<Void> getAndSendCustomerChatStatus(@DestinationVariable Long chatId,
                                                             ChatStatusUpdateRequest chatStatusUpdateRequest,
                                                             SimpMessageHeaderAccessor headerAccessor) {
        try {
            Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());

            chatService.validateChatWithWebSocket(chatId, sessionAttributes, true);

            chatService.getAndSendChatStatus(chatId, sessionAttributes, chatStatusUpdateRequest, true);
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

            chatService.validateChatWithWebSocket(chatId, sessionAttributes, false);

            chatService.getAndSendChatStatus(chatId, sessionAttributes, chatStatusUpdateRequest, false);
        } catch (ChatException | ConsultException e) {
            simpMessagingTemplate.convertAndSend("/queue/chattings/exception/counselors/" + chatId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/api/v1/chat/customers/connect")
    public ResponseEntity<Void> getAndSendCustomerChatIds(SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());
        chatService.getAndSendChatIdsByWebSocket(sessionAttributes, true);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/api/v1/chat/counselors/connect")
    public ResponseEntity<Void> getAndSendCounselorChatIds(SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());
        chatService.getAndSendChatIdsByWebSocket(sessionAttributes, false);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/api/v1/chat/customers/exit/{chatId}")
    public ResponseEntity<Void> leaveCustomerSession(@DestinationVariable Long chatId, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());
        chatService.leaveChatSession(sessionAttributes, chatId, true);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/api/v1/chat/counselors/exit/{chatId}")
    public ResponseEntity<Void> leaveCounselorSession(@DestinationVariable Long chatId, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());
        chatService.leaveChatSession(sessionAttributes, chatId, false);
        return ResponseEntity.ok().build();
    }
}
