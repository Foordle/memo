package com.psu.memo.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 이 엔드포인트로 SockJS 연결을 요청
        registry.addEndpoint("/ws-whiteboard")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /app 으로 오는 메시지는 @MessageMapping 으로 라우팅
        config.setApplicationDestinationPrefixes("/app");
        // /topic 으로 브로드캐스트
        config.enableSimpleBroker("/topic");
    }



}
