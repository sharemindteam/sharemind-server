package com.example.sharemind.chat.application;

import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.domain.ChatUpdateStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatSendService {
    private final ApplicationEventPublisher publisher;
    public void publishEvent(Chat chat, ChatWebsocketStatus chatWebsocketStatus){
        publisher.publishEvent(
                ChatUpdateStatusEvent.of(chat.getChatId(), chatWebsocketStatus));
    }
}
