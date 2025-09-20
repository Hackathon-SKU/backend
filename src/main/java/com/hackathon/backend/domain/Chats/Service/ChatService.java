package com.hackathon.backend.domain.Chats.Service;

import com.hackathon.backend.domain.Chats.Dto.WsAction;
import com.hackathon.backend.domain.Chats.Dto.WsOutbound;
import com.hackathon.backend.domain.Chats.Entity.ChatMessage;
import com.hackathon.backend.domain.Chats.Entity.ChatParticipant;
import com.hackathon.backend.domain.Chats.Entity.ChatRoom;
import com.hackathon.backend.domain.Chats.Entity.MessageType;
import com.hackathon.backend.domain.Chats.Repository.ChatMessageRepository;
import com.hackathon.backend.domain.Chats.Repository.ChatParticipantRepository;
import com.hackathon.backend.domain.Chats.Repository.ChatRoomRepository;
import com.hackathon.backend.domain.Users.Entity.RoleType;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;

    // ✅ matchId로 1:1 방 생성/조회
    public ChatRoom createOrGetRoomByMatch(Long matchId, Long disabledUserId, Long caregiverUserId) {
        return chatRoomRepository.findByMatchId(matchId).orElseGet(() -> {
            var room = chatRoomRepository.save(ChatRoom.builder().matchId(matchId).build());
            chatParticipantRepository.save(ChatParticipant.builder()
                    .room(room).userId(disabledUserId).role(RoleType.DISABLED).build());
            chatParticipantRepository.save(ChatParticipant.builder()
                    .room(room).userId(caregiverUserId).role(RoleType.CAREGIVER).build());
            return room;
        });
    }

    // ✅ 방 참여 확인
    public void assertParticipant(Long roomId, Long userId) {
        if (!chatParticipantRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new IllegalStateException("해당 방의 참여자가 아닙니다. 🚫");
        }
    }

    // ✅ 메시지 전송(저장)
    public ChatMessage sendMessage(Long roomId, Long senderUserId, String content, MessageType type) {
        var room = chatRoomRepository.getReferenceById(roomId);
        assertParticipant(roomId, senderUserId);

        var message = ChatMessage.builder()
                .room(room)
                .senderUserId(senderUserId)
                .content(content)
                .type(type == null ? MessageType.TEXT : type)
                .createdAt(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(message);
    }

    // ✅ 메시지 커서 페이지
    public List<ChatMessage> pageMessages(Long roomId, Long cursor, int size) {
        return chatMessageRepository.pageByRoomDesc(roomId, cursor, PageRequest.of(0, size));
    }

    // ✅ 읽음 처리(메시지 단위 read_at 갱신: 도착 메시지에 대한 “시스템 전체 읽음”으로 사용)
    public ChatMessage markRead(Long messageId) {
        var msg = chatMessageRepository.findById(messageId).orElseThrow();
        msg.markReadNow();
        return msg;
    }

    // ✅ WS Outbound 매핑 helper
    public WsOutbound toOutbound(ChatMessage m, WsAction action) {
        return WsOutbound.builder()
                .action(action)                       // "SENT" | "READ" 등
                .roomId(m.getRoom().getId())
                .messageId(m.getId())
                .senderUserId(m.getSenderUserId())
                .content(m.getContent())
                .type(m.getType().name())
                .createdAt(m.getCreatedAt().toString())
                .readAt(m.getReadAt() == null ? null : m.getReadAt().toString())
                .build();
    }
}
