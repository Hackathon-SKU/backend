package com.hackathon.backend.domain.Chats.Entity;

import com.hackathon.backend.domain.Users.Entity.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_participants",
        uniqueConstraints = @UniqueConstraint(name = "uk_cp_room_user", columnNames = {"room_id","user_id"}))
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "room_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cp_room"),
            columnDefinition = "BIGINT UNSIGNED"     // ✅ 요게 핵심
    )
    private ChatRoom room;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 16)
    private RoleType role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    void onCreate() {
        if (joinedAt == null) joinedAt = LocalDateTime.now();
    }
}