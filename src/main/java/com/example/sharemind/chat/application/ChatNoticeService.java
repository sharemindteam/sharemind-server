package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.content.MessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.exception.ChatMessageErrorCode;
import com.example.sharemind.chatMessage.exception.ChatMessageException;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatNoticeService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void createChatNoticeMessage(Chat chat, MessageStatus messageStatus) {
        if (messageStatus == MessageStatus.SEND_REQUEST) {
            ChatMessage chatMessage = new ChatMessage(chat, false, chat.getConsult().getCustomer().getNickname() + "님, 지금 바로 상담을 시작할까요?", messageStatus);
            chatMessageRepository.save(chatMessage);
        } else if (messageStatus == MessageStatus.START) {
            updateSendRequestMessageIsActivatedFalse(chat);
            ChatMessage chatMessage = new ChatMessage(chat, false, "상담이 시작되었어요.\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a HH시 mm분")), messageStatus);
            chatMessageRepository.save(chatMessage);
        } else if (messageStatus == MessageStatus.FIVE_MINUTE_LEFT) {
            ChatMessage chatMessage = new ChatMessage(chat, false, "상담 종료까지 5분 남았어요.\n" + chat.getStartedAt().plusMinutes(30).format(DateTimeFormatter.ofPattern("a hh시 mm분")), messageStatus);
            chatMessageRepository.save(chatMessage);
        } else if (messageStatus == MessageStatus.TIME_OVER) {
            ChatMessage chatMessage = new ChatMessage(chat, false, "상담 시간이 모두 마무리 되었어요.\n상담이 정상적으로 종료되었다면 상담 종료 버튼을 눌러 주세요.\n*신고접수가 되지 않은 상담 건은 7일 후 자동으로 거래가 확정됩니다.", messageStatus);
            chatMessageRepository.save(chatMessage);
        } else if (messageStatus == MessageStatus.FINISH) {
            ChatMessage chatMessage = new ChatMessage(chat, false, "님과의 상담이 만족스러우셨나요? 후기를 남겨주시면 더 나은 서비스를 위해 큰 도움이 되어요.", messageStatus);
            chatMessageRepository.save(chatMessage);
        }
    }

    @Transactional
    public void updateSendRequestMessageIsActivatedFalse(Chat chat) {
        ChatMessage chatMessage = chatMessageRepository.findByChatAndMessageStatusAndIsActivatedIsTrue(chat, MessageStatus.SEND_REQUEST);
        if (chatMessage == null)
            throw new ChatMessageException(ChatMessageErrorCode.SEND_REQUEST_STATUS_NOT_FOUND);
        chatMessage.updateIsActivatedFalse();
    }
}
