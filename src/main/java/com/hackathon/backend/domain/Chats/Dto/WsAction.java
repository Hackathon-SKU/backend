package com.hackathon.backend.domain.Chats.Dto;

// 웹소켓 요청 액션 상태
public enum WsAction {
    ENTER, // 방 입장 요청
    SEND, // 메시지 보내기 요청
    READ, // 읽음 처리 요청
    
    ENTERED, // 누군가가 방에 들어왔음을 알림
    SENT, // 메시지 저장 완료 && 브로드캐스트
    READ_DONE // 메시지 읽음 처리 반영됨
}
