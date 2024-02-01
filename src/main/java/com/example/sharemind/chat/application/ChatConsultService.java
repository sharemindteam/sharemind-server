package com.example.sharemind.chat.application;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.repository.ChatRepository;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.dto.response.ChatLetterGetOngoingResponse;
import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.sharemind.global.constants.Constants.COUNSELOR_ONGOING_CONSULT;
import static com.example.sharemind.global.constants.Constants.CUSTOMER_ONGOING_CONSULT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatConsultService {

    private final CustomerService customerService;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;


    public ChatLetterGetOngoingResponse getOngoingChats(Long customerId, Boolean isCustomer) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        List<Chat> chats;
        if (isCustomer)
            chats = getRecentChats(CUSTOMER_ONGOING_CONSULT, customer);
        else
            chats = getRecentChats(COUNSELOR_ONGOING_CONSULT, customer);

        List<ChatLetterGetResponse> chatLetterGetResponses = new ArrayList<>();

        for (Chat chat : chats) {
            chatLetterGetResponses.add(createChatInfoGetResponse(chat, isCustomer));
        }

        Integer totalChatOngoing = chatRepository.countChatsByStatusAndCustomer(customer);
        return ChatLetterGetOngoingResponse.of(totalChatOngoing, chatLetterGetResponses);
    }

    private List<Chat> getRecentChats(int count, Customer customer) {
        return chatRepository.findRecentChatsByLatestMessage(PageRequest.of(0, count), customer);
    }

    public ChatLetterGetResponse createChatInfoGetResponse(Chat chat, Boolean isCustomer) {

        Consult consult = chat.getConsult();

        String nickname = isCustomer ? consult.getCounselor().getNickname() : consult.getCustomer().getNickname();

        ChatMessage latestChatMessage = chatMessageRepository.findTopByChatOrderByUpdatedAtDesc(chat);

        Long lastReadMessageId = isCustomer ? chat.getCustomerReadId() : chat.getCounselorReadId();
        int unreadMessageCount = chatMessageRepository.countByChatAndMessageIdGreaterThanAndIsCustomer(
                chat, lastReadMessageId, !isCustomer);

        return ChatLetterGetResponse.of(nickname, unreadMessageCount, chat, consult.getCounselor(), latestChatMessage);
    }
}
