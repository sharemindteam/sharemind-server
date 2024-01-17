package com.example.sharemind.chat.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.dto.request.ChatStatusUpdateRequest;
import com.example.sharemind.chat.dto.response.ChatInfoGetResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

    @MessageMapping("/api/v1/chat/counselors/{chatId}") //request를 보낼 수 있는 웹소켓
    public ResponseEntity<Void> getAndSendChatStatus(@DestinationVariable Long chatId,
                                                     ChatStatusUpdateRequest chatStatusUpdateRequest,
                                                     SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());

        chatService.validateChat(chatId, sessionAttributes, false);

        chatService.getAndSendChatStatus(chatId, chatStatusUpdateRequest, false);
        return ResponseEntity.ok().build();
        //request 보낼 수 있는 거 종류 : 1. counselor가 보내는 상담 요청, 2. customer가 보내는 상담 요청 허락(허락되면 chat status도 업데이트 -> 상담중으로 변화), 3.customer가 상담 종료 request 보내는거
        //eventhandler 만들거 : 5분 남았을 때, 30분 지낫을 때(이때는 다른 response로 보내주어야함), 상담종료가되엇을때 리뷰 요청을 customer로 보내는 이벤트핸들러
        //todo: 전달 받은 status에 따른 채팅 상태 업데이트
    }
}
