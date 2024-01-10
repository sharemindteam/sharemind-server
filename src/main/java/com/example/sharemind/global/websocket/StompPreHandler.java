package com.example.sharemind.global.websocket;

import com.example.sharemind.auth.exception.AuthErrorCode;
import com.example.sharemind.auth.exception.AuthException;
import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.customer.application.CustomerService;
import com.example.sharemind.global.jwt.CustomUserDetails;
import com.example.sharemind.global.jwt.TokenProvider;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final TokenProvider tokenProvider;
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authToken = accessor.getFirstNativeHeader("Authorization");
            if (authToken != null && authToken.startsWith(TOKEN_PREFIX)) {
                String token = authToken.substring(7);
                if (tokenProvider.validateAccessToken(token)) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    String nickname = ((CustomUserDetails) authentication.getPrincipal()).getCustomer().getNickname();
                    Map<String, Object> sessionAttributes = Objects.requireNonNull(accessor.getSessionAttributes());
                    accessor.getSessionAttributes().put("userNickname", nickname);
                    accessor.setSessionAttributes(sessionAttributes);
                    log.info("Session attributes after setting: " + sessionAttributes.toString());
                    return message;
                }
            }
            throw new AuthException(AuthErrorCode.INVALID_ACCESS_TOKEN);
        }
        return message;
    }

    /*demo용
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String userId = accessor.getFirstNativeHeader("userId");
            boolean isCustomer = Boolean.parseBoolean(accessor.getFirstNativeHeader("isCustomer"));
            if (userId != null) {
                String nickname =
                        isCustomer ? customerService.getCustomerByCustomerId(Long.parseLong(userId)).getNickname() :
                                counselorService.getCounselorByCounselorId(Long.parseLong(userId)).getNickname();
                Map<String, Object> sessionAttributes = Objects.requireNonNull(accessor.getSessionAttributes());
                sessionAttributes.put("userId", userId);
                sessionAttributes.put("userNickname", nickname);
                accessor.setSessionAttributes(sessionAttributes);
                log.info("Session attributes after setting: " + sessionAttributes.toString());
            }
            return message;
        }
        return message;
    }
     */
}