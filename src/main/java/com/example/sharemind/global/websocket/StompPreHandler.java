package com.example.sharemind.global.websocket;

import static com.example.sharemind.global.constants.Constants.COUNSELOR_PREFIX;
import static com.example.sharemind.global.constants.Constants.CUSTOMER_PREFIX;
import static com.example.sharemind.global.constants.Constants.TOKEN_PREFIX;

import com.example.sharemind.auth.exception.AuthErrorCode;
import com.example.sharemind.auth.exception.AuthException;
import com.example.sharemind.chat.application.ChatService;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.global.jwt.TokenProvider;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StompPreHandler implements ChannelInterceptor {

    private final CustomerService customerService;
    private final CounselorService counselorService;
    private final ChatService chatService;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, List<Long>> redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            Authentication authentication = authenticateUser(accessor);
            if (authentication != null) {
                setUserDetails(accessor, authentication);
                return message;
            }
            throw new AuthException(AuthErrorCode.INVALID_ACCESS_TOKEN);
        }
        return message;
    }

    private Authentication authenticateUser(StompHeaderAccessor accessor) {
        String authToken = accessor.getFirstNativeHeader("Authorization");
        if (authToken != null && authToken.startsWith(TOKEN_PREFIX)) {
            String token = authToken.substring(TOKEN_PREFIX.length());
            if (tokenProvider.validateAccessToken(token)) {
                return tokenProvider.getAuthentication(token);
            }
        }
        return null;
    }

    private void setUserDetails(StompHeaderAccessor accessor, Authentication authentication) {
        boolean isCustomer = Boolean.parseBoolean(accessor.getFirstNativeHeader("isCustomer"));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = getUserId(userDetails, isCustomer);
        String nickname = getNickname(userId, isCustomer);

        setSessionAttributes(accessor, userId, nickname);

        log.info("Session attributes after setting: " + Objects.requireNonNull(accessor.getSessionAttributes())
                .toString());

        setChatIdsInRedis(userId, isCustomer);
    }

    private void setChatIdsInRedis(Long userId, Boolean isCustomer) {
        List<Long> chatIds = getChatRoomIds(userId, isCustomer);
        String redisKey = (isCustomer ? CUSTOMER_PREFIX : COUNSELOR_PREFIX) + userId;
        redisTemplate.opsForValue().set(redisKey, chatIds);
        log.info("Chat IDs for user {} saved to Redis: {}", userId, chatIds);
    }

    private Long getUserId(CustomUserDetails userDetails, boolean isCustomer) {
        return isCustomer ? userDetails.getCustomer().getCustomerId()
                : userDetails.getCustomer().getCounselor().getCounselorId();
    }//todo: counselorId가 null인 경우 에러 처리

    private String getNickname(Long userId, boolean isCustomer) {
        return isCustomer ? customerService.getCustomerByCustomerId(userId).getNickname()
                : counselorService.getCounselorByCounselorId(userId).getNickname();
    }

    private List<Long> getChatRoomIds(Long userId, boolean isCustomer) {
        return chatService.getChatsByUserId(userId, isCustomer);
    }

    private void setSessionAttributes(StompHeaderAccessor accessor, Long userId,
                                      String nickname) {
        Map<String, Object> sessionAttributes = Objects.requireNonNull(accessor.getSessionAttributes());
        sessionAttributes.put("userNickname", nickname);
        sessionAttributes.put("userId", userId);
        accessor.setSessionAttributes(sessionAttributes);
    }
}
