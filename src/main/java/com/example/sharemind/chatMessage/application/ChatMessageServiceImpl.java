package com.example.sharemind.chatMessage.application;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.chatMessage.dto.response.ChatMessageResponse;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    @Override
    public void createAndSendChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId,
                                         Boolean isCustomer, String nickname) {
        chatService.validateNotFinishChat(chatId);

        createChatMessage(chatMessageCreateRequest, chatId, isCustomer);

        sendChatMessage(chatMessageCreateRequest, chatId, isCustomer, nickname);
    }

    private void sendChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId, Boolean isCustomer,
                                 String nickname) {
        ChatMessageResponse chatMessageResponse = ChatMessageResponse.of(nickname,
                chatMessageCreateRequest.getContent(), isCustomer);
        simpMessagingTemplate.convertAndSend("/queue/chatMessages/counselors/" + chatId, chatMessageResponse);
        simpMessagingTemplate.convertAndSend("/queue/chatMessages/customers/" + chatId, chatMessageResponse);

        log.info("Message [{}] send by member: {} to chatting room: {}", chatMessageCreateRequest.getContent(),
                nickname, chatId);
    }

    @Transactional
    @Override
    public void createChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId,
                                  Boolean isCustomer) {
        Chat chat = chatService.getChatByChatId(chatId);
        chatMessageRepository.save(
                chatMessageCreateRequest.toEntity(chat, isCustomer));
    }
}
