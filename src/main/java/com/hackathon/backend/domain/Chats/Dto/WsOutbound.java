package com.hackathon.backend.domain.Chats.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hackathon.backend.domain.Chats.Entity.MessageType;
import lombok.*;

// 서버-> 클라이언트로 가는 메시지
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 숨김(가벼운 페이로드) ✨
public class WsOutbound {

    /**
     * "ENTERED" | "SENT" | "READ_DONE"
     */
    private WsAction action;

    private Long roomId;
    private Long messageId;
    private Long senderUserId;

    private String content;   // TEXT 메시지 본문
    private String type;      // "TEXT" | "SYSTEM"

    private String createdAt; // ISO 문자열 권장
    private String readAt;    // 읽음 시각(없으면 null)

    // 클라이언트에게 채팅방 입장 완료 메시지를 보내주는 편의성 함수
    public static WsOutbound entered(Long roomId, Long userId) {
        return WsOutbound.builder()
                .action(WsAction.ENTERED)
                .roomId(roomId)
                .senderUserId(userId)
                .build();
    }
}
