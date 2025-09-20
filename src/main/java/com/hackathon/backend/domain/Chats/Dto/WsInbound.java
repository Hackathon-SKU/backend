package com.hackathon.backend.domain.Chats.Dto;


// 클라이언트 -> 서버로 들어오는 메시지

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // 확장성 👍
public class WsInbound {
    /**
     * "ENTER" | "SEND" | "READ"
     */
    private WsAction action;

    /**
     * 대상 채팅방 ID
     */
    private Long roomId;

    /**
     * SEND일 때 메시지 본문
     */
    private String content;

    /**
     * READ일 때 마지막으로 읽은 메시지 ID
     */
    private Long lastReadId;
}