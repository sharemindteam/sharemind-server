package com.example.sharemind.chat.application;

import com.example.sharemind.chat.content.ChatRoomStatus;
import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.domain.ChatNotifyEvent;
import com.example.sharemind.chat.dto.request.ChatStatusUpdateRequest;
import com.example.sharemind.chat.dto.response.ChatGetConnectResponse;
import com.example.sharemind.chat.dto.response.ChatGetStatusResponse;
import com.example.sharemind.chat.exception.ChatErrorCode;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chat.repository.ChatRepository;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import com.example.sharemind.consult.application.ConsultService;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.global.content.ChatLetterSortType;
import com.example.sharemind.global.content.ConsultType;
import com.example.sharemind.global.dto.response.ChatLetterGetResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.sharemind.global.constants.Constants.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    public static final String CUSTOMER_CHATTING_PREFIX = "customer chatting: "; // 현재 접속중인 방
    public static final String COUNSELOR_CHATTING_PREFIX = "counselor chatting: ";

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CounselorService counselorService;
    private final ConsultService consultService;
    private final CustomerService customerService;
    private final ChatConsultService chatConsultService;
    private final ApplicationEventPublisher publisher;
    private final RedisTemplate<String, List<Long>> redisTemplate;
    private final RedisTemplate<String, Map<Long, Integer>> sessionRedisTemplate;
    private final ChatTaskScheduler chatTaskScheduler;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatNoticeService chatNoticeService;

    @Override
    public Chat createChat(Consult consult) {
        Chat chat = Chat.newInstance();
        chatRepository.save(chat);

        updateChatIdsRedis(chat, consult);

        notifyNewChat(chat, consult);

        chatTaskScheduler.checkAutoRefund(chat);
        return chat;
    }

    @Override
    public Chat getChatByChatId(Long chatId) {
        return chatRepository.findByChatIdAndIsActivatedIsTrue(chatId).orElseThrow(() -> new ChatException(
                ChatErrorCode.CHAT_NOT_FOUND, chatId.toString()));
    }

    @Override
    public void updateReadId(Chat chat, Long customerId, Boolean isCustomer) {
        validateChat(chat, isCustomer, customerId);
        ChatMessage chatMessage = chatMessageRepository.findTopByChatAndIsCustomerAndIsActivatedTrueOrderByMessageIdDesc(
                chat, !isCustomer); //customer면 counselor의 가장 위 메세지 아이디를 가져오는거
        Long chatMessageId = (chatMessage != null) ? chatMessage.getMessageId() : 0L;
        if (isCustomer) {
            chat.updateCustomerReadId(chatMessageId);
        } else {
            chat.updateCounselorReadId(chatMessageId);
        }
    }

    @Override
    public void validateChatWithWebSocket(Long chatId, Map<String, Object> sessionAttributes, Boolean isCustomer) {
        Long userId = (Long) sessionAttributes.get("userId");
        String redisKey = isCustomer ? CUSTOMER_PREFIX + userId.toString() : COUNSELOR_PREFIX + userId.toString();

        List<Long> chatRoomIds = redisTemplate.opsForValue()
                .get(redisKey);
        if (chatRoomIds == null || !chatRoomIds.contains(chatId)) {
            throw new ChatException(ChatErrorCode.USER_NOT_IN_CHAT, chatId.toString());
        }
    }

    @Override
    public void getAndSendChatIdsByWebSocket(Map<String, Object> sessionAttributes, Boolean isCustomer) {
        ChatGetConnectResponse chatGetConnectResponse = getChatIds(sessionAttributes, isCustomer);
        sendChatIds(chatGetConnectResponse);
    }

    private ChatGetConnectResponse getChatIds(Map<String, Object> sessionAttributes, Boolean isCustomer) {
        Long userId = (Long) sessionAttributes.get("userId");
        String redisKey = isCustomer ? CUSTOMER_PREFIX + userId.toString() : COUNSELOR_PREFIX + userId.toString();

        List<Long> chatRoomIds = redisTemplate.opsForValue()
                .get(redisKey);
        return ChatGetConnectResponse.of(userId, chatRoomIds);
    }

    private void sendChatIds(ChatGetConnectResponse chatGetConnectResponse) {
        simpMessagingTemplate.convertAndSend("/queue/chattings/connect/counselors/", chatGetConnectResponse);
        simpMessagingTemplate.convertAndSend("/queue/chattings/connect/customers/", chatGetConnectResponse);
    }

    private Long getLatestMessageIdForChat(Chat chat) { //todo: ChatMessageService로 빼고싶었는데 순환참조 문제때문에 못뺌.. 구조 고민해보기
        ChatMessage latestMessage = chatMessageRepository.findTopByChatOrderByUpdatedAtDesc(chat);
        return Optional.ofNullable(latestMessage).map(ChatMessage::getMessageId).orElse(0L);
    }

    @Override
    public void validateChat(Chat chat, Boolean isCustomer, Long customerId) {
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        chat.checkChatAuthority(chat, isCustomer, customer);
    }

    @Override
    public Chat getAndValidateChat(Long chatId, Boolean isCustomer, Long customerId) {
        Chat chat = getChatByChatId(chatId);
        validateChat(chat, isCustomer, customerId);

        return chat;
    }

    @Override
    public void validateNotFinishChat(Long chatId) {
        Chat chat = chatRepository.findByChatIdAndIsActivatedIsTrue(chatId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_NOT_FOUND, chatId.toString()));
        if (chat.getChatStatus() == ChatStatus.FINISH) {
            throw new ChatException(ChatErrorCode.CHAT_STATUS_FINISH, chatId.toString());
        }
    }

    @Override
    public List<ChatLetterGetResponse> getChatInfoByCustomerId(Long customerId, Boolean isCustomer, Boolean filter,
                                                               String chatSortType) {
        ChatLetterSortType sortType = ChatLetterSortType.getSortTypeByName(chatSortType);
        List<Consult> consults;

        if (isCustomer) {
            consults = consultService.getConsultsByCustomerIdAndConsultTypeAndIsPaid(customerId, ConsultType.CHAT);
        } else {
            Counselor counselor = counselorService.getCounselorByCustomerId(customerId);
            consults = consultService.getConsultsByCounselorIdAndConsultTypeAndIsPaid(counselor.getCounselorId(),
                    ConsultType.CHAT);
        }

        if (consults == null) {
            return null;
        }

        List<Chat> filterChats = getFilterChats(consults, filter);

        List<Chat> finalChats = null;

        switch (sortType) {
            case LATEST: {
                finalChats = sortChatsByLatestMessage(filterChats);
                break;
            }
            case UNREAD: {
                finalChats = sortChatsByUnread(filterChats, isCustomer);
                break;
            }
        }
        return finalChats.stream()
                .map(chat -> chatConsultService.createChatInfoGetResponse(chat, isCustomer))
                .collect(Collectors.toList());
    }

    private List<Chat> sortChatsByLatestMessage(List<Chat> chats) {
        Map<Chat, LocalDateTime> latestMessageTime = new HashMap<>();
        for (Chat chat : chats) {
            ChatMessage latestMessage = chatMessageRepository.findTopByChatOrderByUpdatedAtDesc(chat);
            latestMessageTime.put(chat, latestMessage != null ? latestMessage.getUpdatedAt() : LocalDateTime.MIN);
        }

        return chats.stream()
                .sorted(Comparator.comparing((Chat chat) -> latestMessageTime.get(chat)).reversed())
                .collect(Collectors.toList());
    }

    private List<Chat> sortChatsByUnread(List<Chat> filterChats, Boolean isCustomer) {
        Map<Chat, Long> latestMessageIds = filterChats.stream()
                .collect(Collectors.toMap(chat -> chat, this::getLatestMessageIdForChat));
        filterChats.sort((chat1, chat2) -> {
            Long latestMessageId1 = latestMessageIds.get(chat1);
            Long latestMessageId2 = latestMessageIds.get(chat2);
            boolean isUnread1;
            boolean isUnread2;

            if (isCustomer) {
                isUnread1 = chat1.getCustomerReadId() < latestMessageId1;
                isUnread2 = chat2.getCustomerReadId() < latestMessageId2;
            } else {
                isUnread1 = chat1.getCounselorReadId() < latestMessageId1;
                isUnread2 = chat2.getCounselorReadId() < latestMessageId2;
            }
            if (isUnread1 && !isUnread2) {
                return -1;
            }
            if (!isUnread1 && isUnread2) {
                return 1;
            }
            return latestMessageId2.compareTo(latestMessageId1);
        });
        return filterChats;
    }

    private List<Chat> getFilterChats(List<Consult> consults, Boolean filter) {
        if (filter) {
            return consults.stream()
                    .map(Consult::getChat)
                    .filter(chat -> (chat.getChatStatus() != ChatStatus.FINISH && chat.getChatStatus()
                            != ChatStatus.COUNSELOR_CANCEL && chat.getChatStatus() != ChatStatus.CUSTOMER_CANCEL))
                    .collect(Collectors.toList());
        }
        return consults.stream()
                .map(Consult::getChat)
                .collect(Collectors.toList());
    }

    @Override
    public void getAndSendChatStatus(Long chatId, ChatStatusUpdateRequest chatStatusUpdateRequest,
                                     Boolean isCustomer) {
        ChatGetStatusResponse chatGetStatusResponse = getChatStatus(chatId, chatStatusUpdateRequest, isCustomer);

        sendChatStatus(chatId, chatGetStatusResponse);
    }

    private ChatGetStatusResponse getChatStatus(Long chatId, ChatStatusUpdateRequest chatStatusUpdateRequest,
                                                Boolean isCustomer) {
        Chat chat = chatRepository.findByChatIdAndIsActivatedIsTrue(chatId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_NOT_FOUND, chatId.toString()));
        Consult consult = consultService.getConsultByChat(chat);

        validateChatStatusRequest(chat, chatStatusUpdateRequest, isCustomer);

        handleStatusRequest(chat, chatStatusUpdateRequest);

        return ChatGetStatusResponse.of(consult, chatStatusUpdateRequest.getChatWebsocketStatus());
    }

    private void sendChatStatus(Long chatId, ChatGetStatusResponse chatGetStatusResponse) {
        simpMessagingTemplate.convertAndSend("/queue/chattings/counselors/" + chatId, chatGetStatusResponse);
        simpMessagingTemplate.convertAndSend("/queue/chattings/customers/" + chatId, chatGetStatusResponse);

        log.info("status [{}] : chatting room: {}",
                chatGetStatusResponse.getChatWebsocketStatus().toString(), chatId);
    }


    private void handleStatusRequest(Chat chat, ChatStatusUpdateRequest chatStatusUpdateRequest) {

        ChatWebsocketStatus chatWebsocketStatus = chatStatusUpdateRequest.getChatWebsocketStatus();
        switch (chatWebsocketStatus) {
            case COUNSELOR_CHAT_START_REQUEST: { //counselor가 상담 요청을 보낸 상황
                chat.updateChatStatus(ChatStatus.SEND_REQUEST);

                chatNoticeService.createChatNoticeMessage(chat, ChatMessageStatus.SEND_REQUEST);

                chatTaskScheduler.checkSendRequest(chat); //10분을 세는 상황

                if (!chat.getAutoRefund()) // 처음 요청을 보낸거 기준으로 자동환불 처리 해주기 때문에
                    chat.updateAutoRefundTrue();

                break;
            }

            case CUSTOMER_CHAT_START_RESPONSE: {
                chat.updateChatStatus(ChatStatus.ONGOING);

                chatNoticeService.createChatNoticeMessage(chat, ChatMessageStatus.START);

                chatTaskScheduler.checkChatDuration(chat);

                break;
            }

            case CUSTOMER_CHAT_FINISH_REQUEST: { //구매자가 상담 종료를 누른 상황
                chat.updateChatStatus(ChatStatus.FINISH);

                chatNoticeService.createChatNoticeMessage(chat, ChatMessageStatus.FINISH);

                notifyFinishChat(chat, chat.getConsult());
                break;
            }
            default:
                throw new ChatException(ChatErrorCode.INVALID_CHAT_REQUEST, chatWebsocketStatus.toString());
        }
    }

    private void validateChatStatusRequest(Chat chat, ChatStatusUpdateRequest chatStatusUpdateRequest,
                                           Boolean isCustomer) {
        validateChatRoleRequest(chatStatusUpdateRequest, isCustomer);
        validateChatProgressRequest(chat, chatStatusUpdateRequest);
    }

    private void validateChatRoleRequest(ChatStatusUpdateRequest chatStatusUpdateRequest, Boolean isCustomer) {
        String status = chatStatusUpdateRequest.getChatWebsocketStatus().toString();

        if ((isCustomer && status.startsWith("COUNSELOR")) || (!isCustomer && status.startsWith("CUSTOMER"))) {
            throw new ChatException(ChatErrorCode.INVALID_CHAT_ROLE_REQUEST, status);
        }
    }

    private void validateChatProgressRequest(Chat chat, ChatStatusUpdateRequest chatStatusUpdateRequest) {
        ChatWebsocketStatus chatWebsocketStatus = chatStatusUpdateRequest.getChatWebsocketStatus();
        switch (chatWebsocketStatus) {
            case COUNSELOR_CHAT_START_REQUEST: {
                compareChatStatus(chat, ChatStatus.WAITING, chatWebsocketStatus);
                break;
            }
            case CUSTOMER_CHAT_START_RESPONSE: {
                compareChatStatus(chat, ChatStatus.SEND_REQUEST, chatWebsocketStatus);
                break;
            }
            case CUSTOMER_CHAT_FINISH_REQUEST: {
                compareChatStatus(chat, ChatStatus.TIME_OVER, chatWebsocketStatus);
                break;
            }
            default:
                throw new ChatException(ChatErrorCode.INVALID_CHAT_REQUEST, chatWebsocketStatus.toString());
        }
    }

    private void compareChatStatus(Chat chat, ChatStatus expectedStatus,
                                   ChatWebsocketStatus chatWebsocketStatus) {
        if (chat.getChatStatus() != expectedStatus) {
            throw new ChatException(ChatErrorCode.INVALID_CHAT_STATUS_REQUEST, chatWebsocketStatus.toString());
        }
    }

    private void updateChatIdsRedis(Chat chat, Consult consult) {
        updateCustomerRedis(chat, consult);
        updateCounselorRedis(chat, consult);
    }

    private void updateCustomerRedis(Chat chat, Consult consult) {
        String customerKey = CUSTOMER_PREFIX + consult.getCustomer().getCustomerId();
        List<Long> customerChatIds = redisTemplate.opsForValue()
                .get(customerKey);
        if (customerChatIds != null) {
            customerChatIds.add(chat.getChatId()); //todo: 혹시나 모르니 이미 레디스에 해당 방이 있는 상황 검토..
            redisTemplate.opsForValue().set(customerKey, customerChatIds);
            log.info("Updated chat IDs for user {} in Redis: {}", customerKey, customerChatIds);
        }
    }

    private void updateCounselorRedis(Chat chat, Consult consult) {
        String counselorKey = COUNSELOR_PREFIX + consult.getCounselor().getCounselorId();
        List<Long> counselorChatIds = redisTemplate.opsForValue()
                .get(counselorKey);
        if (counselorChatIds != null) {
            counselorChatIds.add(chat.getChatId());
            redisTemplate.opsForValue().set(counselorKey, counselorChatIds);
            log.info("Updated chat IDs for user {} in Redis: {}", counselorKey, counselorChatIds); //todo: 테스트 후 삭제
        }
    }

    private void notifyNewChat(Chat chat, Consult consult) {
        publisher.publishEvent(ChatNotifyEvent.of(chat.getChatId(), consult.getCustomer().getCustomerId(),
                consult.getCounselor().getCounselorId(), ChatRoomStatus.CHAT_ROOM_CREATE));
    }

    private void notifyFinishChat(Chat chat, Consult consult) {
        publisher.publishEvent(ChatNotifyEvent.of(chat.getChatId(), consult.getCustomer().getCustomerId(),
                consult.getCounselor().getCounselorId(), ChatRoomStatus.CHAT_ROOM_FINISH));
    }

    @Override
    public void setChatInSessionRedis(Long chatId, Long customerId, Boolean isCustomer) {
        String redisKey;
        if (isCustomer) {
            redisKey = CUSTOMER_CHATTING_PREFIX + customerId.toString();
        } else {
            redisKey = COUNSELOR_CHATTING_PREFIX + counselorService.getCounselorByCustomerId(customerId).getCounselorId().toString();
        }

        Map<Long, Integer> chatIdCounts = sessionRedisTemplate.opsForValue().get(redisKey);
        if (chatIdCounts == null) {
            chatIdCounts = new HashMap<>();
        }
        chatIdCounts.put(chatId, chatIdCounts.getOrDefault(chatId, 0) + 1);
        sessionRedisTemplate.opsForValue().set(redisKey, chatIdCounts);
    }

    @Override
    public void leaveChatSession(Map<String, Object> sessionAttributes, Long chatId, Boolean isCustomer) {
        Long userId = (Long) sessionAttributes.get("userId");
        Long customerId = userId;
        if (!isCustomer){
            Counselor counselor = counselorService.getCounselorByCounselorId(userId);
            customerId = customerService.getCustomerByCounselor(counselor).getCustomerId();
        }
        String redisKey = isCustomer ? CUSTOMER_CHATTING_PREFIX + userId.toString() : COUNSELOR_CHATTING_PREFIX + userId.toString();
        Map<Long, Integer> chatIdCounts = sessionRedisTemplate.opsForValue().get(redisKey);
        if (chatIdCounts != null && chatIdCounts.containsKey(chatId)) {
            int count = chatIdCounts.get(chatId);
            if (count <= 1) { // 마지막 연결 세션이었을 때
                chatIdCounts.remove(chatId);
                updateReadId(getChatByChatId(chatId), customerId, isCustomer);
            } else {
                chatIdCounts.put(chatId, count - 1);
            }
            if (chatIdCounts.isEmpty()) {
                sessionRedisTemplate.delete(redisKey);
            } else {
                sessionRedisTemplate.opsForValue().set(redisKey, chatIdCounts);
            }
        }
    }
}
