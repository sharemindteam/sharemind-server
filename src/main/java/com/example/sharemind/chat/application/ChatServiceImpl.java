package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.exception.ChatErrorCode;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chat.repository.ChatRepository;
import com.example.sharemind.consult.repository.ConsultRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConsultRepository consultRepository;
    private final ChatRepository chatRepository;

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
}
