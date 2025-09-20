package com.hackathon.backend.domain.Chats.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "room_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_msg_room"),
            columnDefinition = "BIGINT UNSIGNED"     // ✅ 요게 핵심
    )
    private ChatRoom room;

    @Column(name = "sender_user_id", nullable = false)
    private Long senderUserId;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 16)
    private MessageType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (type == null) type = MessageType.TEXT;
    }

    public void markReadNow() { this.readAt = LocalDateTime.now(); }
}