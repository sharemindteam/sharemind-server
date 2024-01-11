package com.example.sharemind.chat.presentation;

import com.example.sharemind.chat.application.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/channels") //todo: 채팅 데모용을 위한 api 삭제해야함
    public ResponseEntity<List<Long>> getChannelList(@RequestParam Long userId, @RequestParam Boolean isCustomer) {
        return ResponseEntity.ok(chatService.getChatsByUserId(userId, isCustomer));
    }
}
