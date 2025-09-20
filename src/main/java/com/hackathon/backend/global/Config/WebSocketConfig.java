package com.hackathon.backend.global.Config;


import com.hackathon.backend.domain.Chats.Handler.ChatSocketHandler;
import com.hackathon.backend.domain.Chats.Interceptor.SimpleAuthHandshakeInterceptor;
import com.hackathon.backend.domain.Chats.Service.ChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatService chatService;

    public WebSocketConfig(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatSocketHandler(), "/ws/chat")
                .addInterceptors(new SimpleAuthHandshakeInterceptor())
                .setAllowedOriginPatterns("http://localhost:*", "https://${NGROK_URL}/*");
    }

    @Bean
    public WebSocketHandler chatSocketHandler() {
        return new ChatSocketHandler(chatService);
    }
}