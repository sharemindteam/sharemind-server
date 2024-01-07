package com.example.sharemind.global.websocket;

import com.example.sharemind.counselor.application.CounselorService;
import com.example.sharemind.customer.application.CustomerService;
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

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StompPreHandler implements ChannelInterceptor {
    private final CustomerService customerService;
    private final CounselorService counselorService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String userId = accessor.getFirstNativeHeader("userId");
            boolean isCustomer = Boolean.parseBoolean(accessor.getFirstNativeHeader("isCustomer"));
            System.out.println("userId : " + userId + "boolean : " + isCustomer);
            if (userId != null) {
                String nickname = isCustomer ? customerService.getCustomerNickname(Long.parseLong(userId)) :
                        counselorService.getCounselorNickname(Long.parseLong(userId));
                System.out.println("nickName : " + nickname);
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
}
