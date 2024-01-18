package com.example.sharemind.chat.application;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.domain.ChatUpdateStatusEvent;
import com.example.sharemind.chat.repository.ChatRepository;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChatTaskScheduler {

    private final TaskScheduler scheduler;
    private final ApplicationEventPublisher publisher;
    private final ChatRepository chatRepository;

    private static final int TEN_MINUTE = 120000; //2분
    private static final int TWENTY_FIVE_MINUTE = 300000; //5분
    private static final int THIRTY_MINUTE = 600000; //10분
//    private static final int TEN_MINUTE = 600000;
//    private static final int TWENTY_FIVE_MINUTE = 1500000;
//    private static final int THIRTY_MINUTE = 1800000;

    public void checkSendRequest(Chat chat) {
        System.out.println("지금 시간 : " + LocalDateTime.now());
        System.out.println("현재 상태 : " + chat.getChatStatus());
        scheduler.schedule(() -> {

            if (chat.getChatStatus() == ChatStatus.SEND_REQUEST) {
                chat.updateChatStatus(ChatStatus.WAITING);
                chatRepository.save(chat);

                publisher.publishEvent(
                        ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_START_REQUEST_CANCEL));
                System.out.println("지금 시간 : " + LocalDateTime.now());
            }
        }, new Date(System.currentTimeMillis() + TEN_MINUTE)); //todo: 스케줄링 실패 에러 핸들링
    }

    public void checkChatDuration(Chat chat) {
        System.out.println("채팅 시작 지금 시간 : " + LocalDateTime.now());
        scheduler.schedule(() -> {
            chat.updateChatStatus(ChatStatus.FIVE_MINUTE_LEFT);
            chatRepository.save(chat);

            publisher.publishEvent(
                    ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_LEFT_FIVE_MINUTE));
            System.out.println("5분 남음 지금 시간 : " + LocalDateTime.now());
        }, new Date(System.currentTimeMillis() + TWENTY_FIVE_MINUTE));

        scheduler.schedule(() -> {
            chat.updateChatStatus(ChatStatus.FINISH);
            chatRepository.save(chat);

            publisher.publishEvent(
                    ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_TIME_OVER));
            System.out.println("채팅종료 지금 시간 : " + LocalDateTime.now());
        }, new Date(System.currentTimeMillis() + THIRTY_MINUTE));
    }
}
