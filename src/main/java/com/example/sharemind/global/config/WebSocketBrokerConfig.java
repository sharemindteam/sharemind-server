package com.example.sharemind.global.config;

import com.example.sharemind.global.websocket.StompErrorHandler;
import com.example.sharemind.global.websocket.StompPreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private final StompPreHandler stompPreHandler;
    private final StompErrorHandler stompErrorHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(("/queue"));
        registry.setApplicationDestinationPrefixes(("/app"));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.setErrorHandler(stompErrorHandler).addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        registration.interceptors(stompPreHandler);
    }
}
