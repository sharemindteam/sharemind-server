package com.example.sharemind.chat.application;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.domain.ChatUpdateStatusEvent;
import com.example.sharemind.chat.exception.ChatErrorCode;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chat.repository.ChatRepository;

import java.util.Date;

import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.domain.Consult;
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
    private final ConsultService consultService;
    private final ChatRepository chatRepository;
    private final ChatNoticeService chatNoticeService;

    private static final int TEN_MINUTE = 60000; //1분
    private static final int TWENTY_FIVE_MINUTE = 120000; //2분
    private static final int THIRTY_MINUTE = 420000; //7분 //테스트용으로 남겨둡니다.
//        private static final int TEN_MINUTE = 600000;
//    private static final int TWENTY_FIVE_MINUTE = 1500000;
//    private static final int THIRTY_MINUTE = 1800000;
    private static final int ONE_DAY = 24 * 60 * 60 * 1000;

    public void checkSendRequest(Chat oldChat) {
        scheduler.schedule(() -> {
            Chat chat = chatRepository.findById(oldChat.getChatId()).orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_NOT_FOUND, oldChat.getChatId().toString()));
            if (chat.getChatStatus() == ChatStatus.SEND_REQUEST) {
                chat.updateChatStatus(ChatStatus.WAITING);
                chatRepository.save(chat);
                chatNoticeService.updateSendRequestMessageIsActivatedFalse(chat);

                publisher.publishEvent(
                        ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_START_REQUEST_CANCEL));
            }
        }, new Date(System.currentTimeMillis() + TEN_MINUTE)); //todo: 스케줄링 실패 에러 핸들링
    }

    public void checkChatDuration(Chat chat) {
        scheduler.schedule(() -> {
            chat.updateChatStatus(ChatStatus.FIVE_MINUTE_LEFT);
            chatRepository.save(chat);

            chatNoticeService.createChatNoticeMessage(chat, ChatMessageStatus.FIVE_MINUTE_LEFT);

            publisher.publishEvent(
                    ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_LEFT_FIVE_MINUTE));
        }, new Date(System.currentTimeMillis() + TWENTY_FIVE_MINUTE));

        scheduler.schedule(() -> {
            chat.updateChatStatus(ChatStatus.TIME_OVER);
            chatRepository.save(chat);

            chatNoticeService.createChatNoticeMessage(chat, ChatMessageStatus.TIME_OVER);

            publisher.publishEvent(
                    ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_TIME_OVER));
        }, new Date(System.currentTimeMillis() + THIRTY_MINUTE));
    }

    @Transactional
    public void checkAutoRefund(Chat oldChat) {
        scheduler.schedule(() -> {
            Chat chat = chatRepository.findById(oldChat.getChatId()).orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_NOT_FOUND, oldChat.getChatId().toString()));
            if (chat.getAutoRefund()) {

                Consult consult = consultService.getConsultByChat(chat);

                consult.updateConsultStatusCounselorCancel();

                publisher.publishEvent(ChatUpdateStatusEvent.of(chat.getChatId(), ChatWebsocketStatus.CHAT_CANCEL));
            }
        }, new Date(System.currentTimeMillis() + ONE_DAY));
    }
}
