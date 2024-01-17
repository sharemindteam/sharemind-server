package com.example.sharemind.chat.application;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.domain.Chat;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatTaskScheduler {

    private final TaskScheduler scheduler;

    private static final int TEN_MINUTE = 600000;
    private static final int TWENTY_FIVE_MINUTE = 1500000;
    private static final int THIRTY_MINUTE = 1800000;

    public void checkSendRequest(Chat chat) {
        scheduler.schedule(() -> {

            if (chat.getChatStatus() == ChatStatus.SEND_REQUEST) {
                chat.updateChatStatus(ChatStatus.WAITING);
                //todo: eventlistner 상담 시간 취소 로직으로 변경
            }
        }, new Date(System.currentTimeMillis() + TEN_MINUTE)); //todo: 스케줄링 실패 에러 핸들링
    }

    public void checkChatDuration(Chat chat) {
        scheduler.schedule(() -> {
            chat.updateChatStatus(ChatStatus.FIVE_MINUTE_LEFT);

            //todo:eventListener 발생
        }, new Date(System.currentTimeMillis() + TWENTY_FIVE_MINUTE));

        scheduler.schedule(() -> {
            chat.updateChatStatus(ChatStatus.FINISH);
            //todo:eventListener 발생
        }, new Date(System.currentTimeMillis() + THIRTY_MINUTE));
    }
}
