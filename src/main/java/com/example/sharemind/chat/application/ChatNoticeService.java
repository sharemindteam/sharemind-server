package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.dto.response.ChatMessageWebSocketResponse;
import com.example.sharemind.chatMessage.exception.ChatMessageErrorCode;
import com.example.sharemind.chatMessage.exception.ChatMessageException;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatNoticeService {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public void createChatNoticeMessage(Chat chat, ChatMessageStatus chatMessageStatus) {
        ChatMessage chatMessage = null;
        if (chatMessageStatus == ChatMessageStatus.SEND_REQUEST) {
            chatMessage = new ChatMessage(chat, false, chat.getConsult().getCustomer().getNickname() + "님, 지금 바로 상담을 시작할까요?", chatMessageStatus);
        } else if (chatMessageStatus == ChatMessageStatus.START) {
            updateSendRequestMessageIsActivatedFalse(chat);
            chatMessage = new ChatMessage(chat, true, "상담이 시작되었어요.\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a HH시 mm분")), chatMessageStatus);
        } else if (chatMessageStatus == ChatMessageStatus.FIVE_MINUTE_LEFT) {
            chatMessage = new ChatMessage(chat, null, "상담 종료까지 5분 남았어요.\n" + chat.getStartedAt().plusMinutes(30).format(DateTimeFormatter.ofPattern("a hh시 mm분")), chatMessageStatus);
        } else if (chatMessageStatus == ChatMessageStatus.TIME_OVER) {
            chatMessage = new ChatMessage(chat, null, "상담 시간이 모두 마무리 되었어요.\n상담이 정상적으로 종료되었다면 상담 종료 버튼을 눌러 주세요.\n*신고접수가 되지 않은 상담 건은 7일 후 자동으로 거래가 확정됩니다.", chatMessageStatus);
        } else if (chatMessageStatus == ChatMessageStatus.FINISH) {
            chatMessage = new ChatMessage(chat, null, "님과의 상담이 만족스러우셨나요? 후기를 남겨주시면 더 나은 서비스를 위해 큰 도움이 되어요.", chatMessageStatus);
        }
        if (chatMessage != null) {
            chatMessageRepository.save(chatMessage);
            String opponentNickname = getOpponentNickname(chat, chatMessage.getIsCustomer());
            sendChatNoticeMessage(chatMessage, opponentNickname, chat.getChatId());
        }
    }

    @Transactional
    public void updateSendRequestMessageIsActivatedFalse(Chat chat) {
        ChatMessage chatMessage = chatMessageRepository.findByChatAndMessageStatusAndIsActivatedTrue(chat, ChatMessageStatus.SEND_REQUEST);
        if (chatMessage == null)
            throw new ChatMessageException(ChatMessageErrorCode.SEND_REQUEST_STATUS_NOT_FOUND);
        chatMessage.updateIsActivatedFalse();
    }

    private void sendChatNoticeMessage(ChatMessage chatMessage, String opponentNickname, Long chatId) {
        ChatMessageWebSocketResponse chatMessageWebSocketResponse = ChatMessageWebSocketResponse.of(opponentNickname, chatMessage.getContent(), chatMessage.getIsCustomer());
        simpMessagingTemplate.convertAndSend("/queue/chatMessages/counselors/" + chatId, chatMessageWebSocketResponse);
        simpMessagingTemplate.convertAndSend("/queue/chatMessages/customers/" + chatId, chatMessageWebSocketResponse);

        log.info("Message [{}] send by member: {} to chatting room: {}", chatMessage.getContent(),
                opponentNickname, chatId);
    }

    private String getOpponentNickname(Chat chat, Boolean isCustomer) {
        if (isCustomer == null)
            return null;
        else if (isCustomer)
            return chat.getConsult().getCounselor().getNickname();
        else
            return chat.getConsult().getCustomer().getNickname();
    }
}
