package com.hackathon.backend.domain.Chats.Controller;

import java.util.List;

import com.hackathon.backend.domain.Chats.Entity.ChatMessage;
import com.hackathon.backend.domain.Chats.Entity.ChatRoom;
import com.hackathon.backend.domain.Chats.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    // 방 생성/조회 (matchId 1:1)
    @PostMapping("/rooms/by-match/{matchId}")
    public ChatRoom createOrGetRoom(@PathVariable Long matchId,
                                    @RequestParam Long disabledUserId,
                                    @RequestParam Long caregiverUserId) {
        return chatService.createOrGetRoomByMatch(matchId, disabledUserId, caregiverUserId);
    }

    // 메시지 페이지
    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatMessage> page(@PathVariable Long roomId,
                                  @RequestParam(required = false) Long cursor,
                                  @RequestParam(defaultValue = "50") int size) {
        return chatService.pageMessages(roomId, cursor, size);
    }
}
