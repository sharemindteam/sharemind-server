package com.example.sharemind.global.websocket;

import com.example.sharemind.chat.content.ChatRoomWebsocketStatus;
import com.example.sharemind.chat.domain.ChatNotifyEvent;
import com.example.sharemind.chat.domain.ChatUpdateStatusEvent;
import com.example.sharemind.chat.dto.response.ChatNotifyEventResponse;
import com.example.sharemind.chat.dto.response.ChatUpdateStatusResponse;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String nickname = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("userNickname");
        log.info("{} disconnected", nickname);
    }

    @EventListener
    public void handleChatNotifyEvent(ChatNotifyEvent chatNotifyEvent) {
        System.out.println("customerId : " + chatNotifyEvent.getCustomerId() + "\ncounselorId: "
                + chatNotifyEvent.getCounselorId()); //todo: 로깅용으로 찍은 것.. 채팅 연결 후 삭제
        messageSendingOperations.convertAndSend(
                "/queue/chattings/notifications/customers/" + chatNotifyEvent.getCustomerId(),
                ChatNotifyEventResponse.of(chatNotifyEvent.getChatId(), ChatRoomWebsocketStatus.CHAT_ROOM_CREATE));
        messageSendingOperations.convertAndSend(
                "/queue/chattings/notifications/counselors/" + chatNotifyEvent.getCounselorId(),
                ChatNotifyEventResponse.of(chatNotifyEvent.getChatId(), ChatRoomWebsocketStatus.CHAT_ROOM_CREATE));
    }

    @EventListener
    public void handleChatTimeEvent(ChatUpdateStatusEvent chatUpdateStatusEvent) {
        messageSendingOperations.convertAndSend(
                "/queue/chattings/status/customers/" + chatUpdateStatusEvent.getChatId(),
                ChatUpdateStatusResponse.of(chatUpdateStatusEvent.getChatWebsocketStatus()));
        messageSendingOperations.convertAndSend(
                "/queue/chattings/status/counselors/" + chatUpdateStatusEvent.getChatId(),
                ChatUpdateStatusResponse.of(chatUpdateStatusEvent.getChatWebsocketStatus()));
    }
}
