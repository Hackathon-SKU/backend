package com.hackathon.backend.domain.Chats.Handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.backend.domain.Chats.Dto.WsAction;
import com.hackathon.backend.domain.Chats.Dto.WsInbound;
import com.hackathon.backend.domain.Chats.Dto.WsOutbound;
import com.hackathon.backend.domain.Chats.Entity.ChatMessage;
import com.hackathon.backend.domain.Chats.Entity.MessageType;
import com.hackathon.backend.domain.Chats.Service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class ChatSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper om = new ObjectMapper();
    private final ChatService chatService;

    // roomId -> 세션들
    private final Map<Long, Set<WebSocketSession>> roomSessions = new HashMap<>();

    private Long userId(WebSocketSession session) {
        return (Long) session.getAttributes().get("userId");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("✅ WS connected: {}", userId(session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var inbound = om.readValue(message.getPayload(), WsInbound.class);
        var me = userId(session);

        switch (inbound.getAction()) {
            case WsAction.ENTER -> {
                chatService.assertParticipant(inbound.getRoomId(), me);
                roomSessions.computeIfAbsent(inbound.getRoomId(), k -> new HashSet<>()).add(session);

                // ✅ class 빌더/팩토리 사용
                var out = WsOutbound.entered(inbound.getRoomId(), me);
                broadcast(inbound.getRoomId(), out, null);
            }
            case WsAction.SEND -> {
                // 간단 검증
                if (inbound.getContent() == null || inbound.getContent().isBlank()) return;

                var saved = chatService.sendMessage(
                        inbound.getRoomId(), me, inbound.getContent(), MessageType.TEXT);

                var out = chatService.toOutbound(saved, WsAction.SENT);
                broadcast(inbound.getRoomId(), out, null);
            }
            case WsAction.READ -> {
                if (inbound.getLastReadId() != null) {
                    var updated = chatService.markRead(inbound.getLastReadId());
                    var out = chatService.toOutbound(updated, WsAction.READ_DONE);
                    broadcast(updated.getRoom().getId(), out, null);
                }
            }
            default -> log.warn("❓ Unknown action: {}", inbound.getAction());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        var uid = userId(session);
        roomSessions.values().forEach(set -> set.remove(session));
        log.info("👋 WS closed: {}", uid);
    }

    private void broadcast(Long roomId, WsOutbound payload, WebSocketSession exclude) {
        var sessions = roomSessions.getOrDefault(roomId, Set.of());
        var text = toText(payload);
        for (var s : sessions) {
            if (exclude != null && s.getId().equals(exclude.getId())) continue;
            try { if (s.isOpen()) s.sendMessage(text); } catch (Exception ignored) {}
        }
    }

    private TextMessage toText(Object o) {
        try { return new TextMessage(om.writeValueAsString(o)); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}