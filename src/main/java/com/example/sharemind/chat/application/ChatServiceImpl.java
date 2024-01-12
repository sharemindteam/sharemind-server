package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.dto.response.ChatInfoGetResponse;
import com.example.sharemind.chat.exception.ChatErrorCode;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chat.repository.ChatRepository;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.repository.ConsultRepository;
import com.example.sharemind.global.content.ConsultType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConsultRepository consultRepository;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public List<Long> getChatsByUserId(Long userId, Boolean isCustomer) {
        if (isCustomer) {
            return consultRepository.findChatIdsByCustomerId(userId);
        } else {
            return consultRepository.findChatIdsByCounselorId(userId);
        }
    }

    @Override
    public Chat getChatByChatId(Long chatId) {
        return chatRepository.findByChatIdAndIsActivatedIsTrue(chatId).orElseThrow(() -> new ChatException(
                ChatErrorCode.CHAT_NOT_FOUND, chatId.toString()));
    }

    @Override
    public void validateChat(Map<String, Object> sessionAttributes, Long chatId) {
        List<Long> chatRoomIds = (List<Long>) sessionAttributes.get("chatRoomIds");
        if (!chatRoomIds.contains(chatId)) {
            throw new ChatException(ChatErrorCode.USER_NOT_IN_CHAT, chatId.toString());
        }
    }

    @Override
    public List<ChatInfoGetResponse> getChatInfoByCustomerId(Long customerId, Boolean isCustomer) {
        List<Consult> consults = consultRepository.findByCustomerIdAndConsultTypeAndIsPaid(customerId,
                ConsultType.CHAT);
        return consults.stream()
                .map(consult -> createChatInfoGetResponse(consult, isCustomer))
                .collect(Collectors.toList());
    }

    private ChatInfoGetResponse createChatInfoGetResponse(Consult consult, Boolean isCustomer) {

        Chat chat = consult.getChat();

        String nickname = isCustomer ? consult.getCounselor().getNickname() : consult.getCustomer().getNickname();

        ChatMessage latestChatMessage = chatMessageRepository.findLatestByChatOrderByUpdatedAtDesc(chat);

        Long lastReadMessageId = isCustomer ? chat.getCustomerReadId() : chat.getCounselorReadId();
        int unreadMessageCount = chatMessageRepository.countByChat_ChatIdAndMessageIdGreaterThanAndIsCustomer(
                chat.getChatId(), lastReadMessageId, isCustomer);

        return ChatInfoGetResponse.of(nickname, unreadMessageCount, chat, latestChatMessage);
    }
}
