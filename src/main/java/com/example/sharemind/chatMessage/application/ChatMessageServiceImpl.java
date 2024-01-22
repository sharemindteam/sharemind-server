package com.example.sharemind.chatMessage.application;

import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.dto.request.ChatMessageCreateRequest;
import com.example.sharemind.chatMessage.dto.response.ChatMessageGetResponse;
import com.example.sharemind.chatMessage.dto.response.ChatMessageWebSocketResponse;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
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
    private final CustomerService customerService;
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
        ChatMessageWebSocketResponse chatMessageWebSocketResponse = ChatMessageWebSocketResponse.of(nickname,
                chatMessageCreateRequest.getContent(), isCustomer);
        simpMessagingTemplate.convertAndSend("/queue/chatMessages/counselors/" + chatId, chatMessageWebSocketResponse);
        simpMessagingTemplate.convertAndSend("/queue/chatMessages/customers/" + chatId, chatMessageWebSocketResponse);

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

    @Override
    public List<ChatMessageGetResponse> getChatMessage(Long chatId, Long messageId, Long customerId,
                                                       Boolean isCustomer) {
        Chat chat = chatService.getChatByChatId(chatId);

        chatService.validateChat(chat, isCustomer, customerId);

        List<ChatMessage> chatMessages = getChatMessageByPagination(chat, messageId);
        return chatMessages.stream()
                .map(chatMessage -> ChatMessageGetResponse.of(chat, chatMessage))
                .toList();
    }

    private List<ChatMessage> getChatMessageByPagination(Chat chat, Long messageId) {
        Pageable pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, CHAT_MESSAGE_PAGE);
        Page<ChatMessage> page = chatMessageRepository.findByChatAndMessageIdLessThanOrderByMessageIdDesc(chat,
                messageId, pageable);
        return page.getContent();
    }
}
