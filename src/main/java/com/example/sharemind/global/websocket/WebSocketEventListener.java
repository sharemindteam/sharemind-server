package com.example.sharemind.global.websocket;

import com.example.sharemind.chat.content.ChatRoomStatus;
import com.example.sharemind.chat.domain.ChatCreateAndFinishEvent;
import com.example.sharemind.chat.domain.ChatUpdateStatusEvent;
import com.example.sharemind.chat.dto.response.ChatCreateEventResponse;
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
    public void handleChatCreateEvent(ChatCreateAndFinishEvent chatCreateEvent) {
        System.out.println("customerId : " + chatCreateEvent.getCustomerId() + "\ncounselorId: "
                + chatCreateEvent.getCounselorId()); //todo: 로깅용으로 찍은 것.. 채팅 연결 후 삭제
        messageSendingOperations.convertAndSend(
                "/queue/chattings/notifications/customers/" + chatCreateEvent.getCustomerId(),
                ChatCreateEventResponse.of(chatCreateEvent.getChatId(), ChatRoomStatus.CHAT_ROOM_CREATE));
        messageSendingOperations.convertAndSend(
                "/queue/chattings/notifications/counselors/" + chatCreateEvent.getCounselorId(),
                ChatCreateEventResponse.of(chatCreateEvent.getChatId(), ChatRoomStatus.CHAT_ROOM_CREATE));
    }

    @EventListener
    public void handleChatFinishEvent(ChatCreateAndFinishEvent chatCreateAndFinishEvent) {
        messageSendingOperations.convertAndSend(
                "/queue/chattings/notifications/customers/" + chatCreateAndFinishEvent.getCustomerId(),
                ChatCreateEventResponse.of(chatCreateAndFinishEvent.getChatId(), ChatRoomStatus.CHAT_ROOM_FINISH));
        messageSendingOperations.convertAndSend(
                "/queue/chattings/notifications/counselors/" + chatCreateAndFinishEvent.getCounselorId(),
                ChatCreateEventResponse.of(chatCreateAndFinishEvent.getChatId(), ChatRoomStatus.CHAT_ROOM_FINISH));
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
