package com.hackathon.backend.domain.Chats.Interceptor;


import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class SimpleAuthHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpHeaders headers = request.getHeaders();
        String userId = headers.getFirst("X-User-Id");
        if (userId == null) return false; // 인증 실패
        attributes.put("userId", Long.parseLong(userId));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {}
}

