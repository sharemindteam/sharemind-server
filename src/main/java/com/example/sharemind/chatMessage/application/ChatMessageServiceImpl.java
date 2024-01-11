package com.example.sharemind.chatMessage.application;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void createChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId, Boolean isCustomer) {
        Chat chat = chatService.getChatByChatId(chatId);
        chatMessageRepository.save(
                chatMessageCreateRequest.toEntity(chat, isCustomer));
    }
}
