package com.example.sharemind.chat.presentation;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.dto.response.ChatInfoGetResponse;
import com.example.sharemind.global.jwt.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/chat")
    public ResponseEntity<List<ChatInfoGetResponse>> getChatList(@RequestParam Boolean isCustomer,
                                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<ChatInfoGetResponse> chatInfoGetResponses = chatService.getChatInfoByCustomerId(
                customUserDetails.getCustomer().getCustomerId(), isCustomer);
        return ResponseEntity.ok(chatInfoGetResponses);
        //todo: 메세지 읽지 않은 순, 완료/취소된 상담 포함한 것도 구현해야함ㅜㅜ
    }
}
