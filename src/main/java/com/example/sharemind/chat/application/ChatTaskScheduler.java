package com.example.sharemind.chat.application;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.domain.ChatUpdateStatusEvent;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatTaskScheduler {

    private final TaskScheduler scheduler;
    private final ApplicationEventPublisher publisher;

    private static final int TEN_MINUTE = 600000;
    private static final int TWENTY_FIVE_MINUTE = 1500000;
    private static final int THIRTY_MINUTE = 1800000;

    public void checkSendRequest(Chat chat) {
        scheduler.schedule(() -> {

            if (chat.getChatStatus() == ChatStatus.SEND_REQUEST) {
                chat.updateChatStatus(ChatStatus.WAITING);

                publisher.publishEvent(
                        ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_START_REQUEST_CANCEL));
            }
        }, new Date(System.currentTimeMillis() + TEN_MINUTE)); //todo: 스케줄링 실패 에러 핸들링
    }

    public void checkChatDuration(Chat chat) {
        scheduler.schedule(() -> {
            chat.updateChatStatus(ChatStatus.FIVE_MINUTE_LEFT);

            publisher.publishEvent(
                    ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_LEFT_FIVE_MINUTE));
        }, new Date(System.currentTimeMillis() + TWENTY_FIVE_MINUTE));

        scheduler.schedule(() -> {
            chat.updateChatStatus(ChatStatus.FINISH);

            publisher.publishEvent(
                    ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_TIME_OVER));
        }, new Date(System.currentTimeMillis() + THIRTY_MINUTE));
    }
}
