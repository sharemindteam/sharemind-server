package com.example.sharemind.chat.application;

import static com.example.sharemind.global.constants.Constants.COUNSELOR_PREFIX;
import static com.example.sharemind.global.constants.Constants.CUSTOMER_PREFIX;

import com.example.sharemind.chat.content.ChatStatus;
import com.example.sharemind.chat.content.ChatWebsocketStatus;
import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chat.domain.ChatCreateEvent;
import com.example.sharemind.chat.dto.request.ChatStatusUpdateRequest;
import com.example.sharemind.chat.dto.response.ChatGetStatusResponse;
import com.example.sharemind.chat.dto.response.ChatInfoGetResponse;
import com.example.sharemind.chat.exception.ChatErrorCode;
import com.example.sharemind.chat.exception.ChatException;
import com.example.sharemind.chat.repository.ChatRepository;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import com.example.sharemind.chatMessage.repository.ChatMessageRepository;
import com.example.sharemind.consult.domain.Consult;
import com.example.sharemind.consult.exception.ConsultErrorCode;
import com.example.sharemind.consult.exception.ConsultException;
import com.example.sharemind.consult.repository.ConsultRepository;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.content.ConsultType;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConsultRepository consultRepository;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CounselorService counselorService;
    private final ApplicationEventPublisher publisher;
    private final RedisTemplate<String, List<Long>> redisTemplate;
    private final ChatTaskScheduler chatTaskScheduler;

    @Transactional
    @Override
    public Chat createChat(Consult consult) {
        Chat chat = Chat.newInstance();
        chatRepository.save(chat);

        updateChatIdsRedis(chat, consult);

        notifyNewChat(chat, consult);
        return chat;
    }

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
    public void validateChat(Long chatId, Map<String, Object> sessionAttributes, Boolean isCustomer) {

        String userId = (String) sessionAttributes.get("userId");
        String redisKey = isCustomer ? CUSTOMER_PREFIX + userId : COUNSELOR_PREFIX + userId;
        List<Long> chatRoomIds = redisTemplate.opsForValue()
                .get(redisKey);
        if (chatRoomIds == null || !chatRoomIds.contains(chatId)) {
            throw new ChatException(ChatErrorCode.USER_NOT_IN_CHAT, chatId.toString());
        }
    }

    @Override
    public List<ChatInfoGetResponse> getChatInfoByCustomerId(Long customerId, Boolean isCustomer) {
        List<Consult> consults;
        if (isCustomer) {
            consults = consultRepository.findByCustomerIdAndConsultTypeAndIsPaid(customerId, ConsultType.CHAT);
        } else {
            Counselor counselor = counselorService.getCounselorByCustomerId(customerId);
            consults = consultRepository.findByCounselorIdAndConsultTypeAndIsPaid(counselor.getCounselorId(),
                    ConsultType.CHAT);
        }
        return consults.stream()
                .map(consult -> createChatInfoGetResponse(consult, isCustomer))
                .toList();
    }

    @Override
    public ChatGetStatusResponse getAndUpdateChatStatus(Long chatId, ChatStatusUpdateRequest chatStatusUpdateRequest,
                                                        Boolean isCustomer) {
        Chat chat = chatRepository.findByChatIdAndIsActivatedIsTrue(chatId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_NOT_FOUND, chatId.toString()));
        Consult consult = consultRepository.findByChatAndIsActivatedIsTrue(chat).orElseThrow(
                () -> new ConsultException(ConsultErrorCode.CONSULT_NOT_FOUND, "chatId : " + chatId.toString()));

        validateChatStatusRequest(chat, chatStatusUpdateRequest, isCustomer);

        handleStatusRequest(chat, chatStatusUpdateRequest);

        return ChatGetStatusResponse.of(consult, chatStatusUpdateRequest.getChatWebsocketStatus());
    }

    private void handleStatusRequest(Chat chat, ChatStatusUpdateRequest chatStatusUpdateRequest) {

        ChatWebsocketStatus chatWebsocketStatus = chatStatusUpdateRequest.getChatWebsocketStatus();
        switch (chatWebsocketStatus) {
            case COUNSELOR_CHAT_START_REQUEST: { //counselor가 상담 요청을 보낸 상황
                chat.updateChatStatus(ChatStatus.SEND_REQUEST);

                chatTaskScheduler.checkSendRequest(chat); //10분을 세는 상황
            }
            case CUSTOMER_CHAT_START_RESPONSE: {
                chat.updateChatStatus(ChatStatus.ONGOING);
                chat.updateStartedAt();

                chatTaskScheduler.checkChatDuration(chat);
            }

            case CUSTOMER_CHAT_FINISH_REQUEST: { //구매자가 상담 종료를 누른 상황
                chat.updateChatStatus(ChatStatus.FINISH);
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
            }
            case CUSTOMER_CHAT_START_RESPONSE: {
                compareChatStatus(chat, ChatStatus.SEND_REQUEST, chatWebsocketStatus);
            }
            case CUSTOMER_CHAT_FINISH_REQUEST: {
                compareChatStatus(chat, ChatStatus.TIME_OVER, chatWebsocketStatus);
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

    private ChatInfoGetResponse createChatInfoGetResponse(Consult consult, Boolean isCustomer) {

        Chat chat = consult.getChat();

        String nickname = isCustomer ? consult.getCounselor().getNickname() : consult.getCustomer().getNickname();

        ChatMessage latestChatMessage = chatMessageRepository.findTopByChatOrderByUpdatedAtDesc(chat);

        Long lastReadMessageId = isCustomer ? chat.getCustomerReadId() : chat.getCounselorReadId();
        int unreadMessageCount = chatMessageRepository.countByChatAndMessageIdGreaterThanAndIsCustomer(
                chat, lastReadMessageId, !isCustomer);

        return ChatInfoGetResponse.of(nickname, unreadMessageCount, chat, latestChatMessage);
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
            log.info("Updated chat IDs for user {} in Redis: {}", customerKey, customerChatIds); //todo: 테스트 후 삭제
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
        publisher.publishEvent(ChatCreateEvent.of(chat.getChatId(), consult.getCustomer().getCustomerId(),
                consult.getCounselor().getCounselorId()));
    }
}
