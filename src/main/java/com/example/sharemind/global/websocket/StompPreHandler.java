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
            String destination = accessor.getDestination();
            if ("/customerChat".equals(destination)) {
                String userId = accessor.getFirstNativeHeader("userId");
                if (userId != null) {
                    String customerNickname = customerService.getCustomerNickname(Long.parseLong(userId));
                    Map<String, Object> sessionAttributes = Objects.requireNonNull(accessor.getSessionAttributes());
                    sessionAttributes.put("customerId", userId);
                    sessionAttributes.put("customerNickname", customerNickname);
                }
            } else if ("/counselorChat".equals(destination)) {
                String userId = accessor.getFirstNativeHeader("userId");
                if (userId != null) {
                    String counselorNickName = counselorService.getCounselorNickname(Long.parseLong(userId));
                    Map<String, Object> sessionAttributes = Objects.requireNonNull(accessor.getSessionAttributes());
                    sessionAttributes.put("counselorId", userId);
                    sessionAttributes.put("counselorNickname", counselorNickName);
                }
            }
            return message;
        }
        return null;
    }
}
