package com.example.sharemind.chatMessage.application;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.chatMessage.dto.response.ChatMessageGetResponse;
import com.example.sharemind.chatMessage.dto.response.ChatMessageWebSocketResponse;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int CHAT_MESSAGE_PAGE = 11;

    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    @Override
    public void createAndSendChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId,
                                         Boolean isCustomer, String nickname) {
        chatService.validateNotFinishChat(chatId);

        createChatMessage(chatMessageCreateRequest, chatId, isCustomer);

        sendChatMessage(chatMessageCreateRequest.getContent(), chatId, isCustomer, nickname);
    }

    private void sendChatMessage(String content, Long chatId, Boolean isCustomer,
                                 String nickname) {
        ChatMessageWebSocketResponse chatMessageWebSocketResponse = ChatMessageWebSocketResponse.of(nickname, content, isCustomer);
        simpMessagingTemplate.convertAndSend("/queue/chatMessages/counselors/" + chatId, chatMessageWebSocketResponse);
        simpMessagingTemplate.convertAndSend("/queue/chatMessages/customers/" + chatId, chatMessageWebSocketResponse);

        log.info("Message [{}] send by member: {} to chatting room: {}", content,
                nickname, chatId);
    }

    @Transactional
    @Override
    public void createChatMessage(ChatMessageCreateRequest chatMessageCreateRequest, Long chatId,
                                  Boolean isCustomer) {
        Chat chat = chatService.getChatByChatId(chatId);
        chatMessageRepository.save(
                chatMessageCreateRequest.toEntity(chat, isCustomer, ChatMessageStatus.MESSAGE));
    }

    @Override
    public List<ChatMessageGetResponse> getChatMessage(Long chatId, Long messageId, Long customerId,
                                                       Boolean isCustomer) {
        Chat chat = chatService.getChatByChatId(chatId);

        chatService.validateChat(chat, isCustomer, customerId);

        if (messageId == 0) {//처음 들어왔을 때만 갱신
            chatService.updateReadId(chat, customerId, isCustomer);

            chatService.setChatInSessionRedis(chatId, customerId, isCustomer);
        }
        List<ChatMessage> chatMessages = getChatMessageByPagination(chat, messageId);
        return chatMessages.stream()
                .map(chatMessage -> ChatMessageGetResponse.of(chat, chatMessage, isCustomer))
                .toList();
    }

    private List<ChatMessage> getChatMessageByPagination(Chat chat, Long messageId) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, CHAT_MESSAGE_PAGE);
        Page<ChatMessage> page = (messageId == 0)
                ? chatMessageRepository.findByChatAndIsActivatedTrueOrderByMessageIdDesc(chat, pageable)
                : chatMessageRepository.findByChatAndIsActivatedTrueAndMessageIdLessThanOrderByMessageIdDesc(chat, messageId, pageable);
        return page.getContent();
    }
}
