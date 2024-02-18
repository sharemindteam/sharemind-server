package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.exception.ChatErrorCode;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatNoticeService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void createChatNoticeMessage(Chat chat, ChatMessageStatus chatMessageStatus) {
        if (chatMessageStatus == ChatMessageStatus.SEND_REQUEST) {
            ChatMessage chatMessage = new ChatMessage(chat, false, chat.getConsult().getCustomer().getNickname() + "님, 지금 바로 상담을 시작할까요?", chatMessageStatus);
            chatMessageRepository.save(chatMessage);
        } else if (chatMessageStatus == ChatMessageStatus.START) {
            updateSendRequestMessageIsActivatedFalse(chat);
            ChatMessage chatMessage = new ChatMessage(chat, false, "상담이 시작되었어요. \n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 a")), chatMessageStatus);
            chatMessageRepository.save(chatMessage);
        }
    }

    @Transactional
    public void updateSendRequestMessageIsActivatedFalse(Chat chat) {
        ChatMessage chatMessage = chatMessageRepository.findByMessageStatusAndChatAndIsActivatedTrue(chat, ChatMessageStatus.SEND_REQUEST);
        if (chatMessage == null)
            throw new ChatException(ChatErrorCode.CHAT_STATUS_NOT_FOUND, "채팅 요청 존재하지 않음");
        chatMessage.updateIsActivatedFalse();
    }
}
