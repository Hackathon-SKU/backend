package com.hackathon.backend.domain.Match.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches",
        uniqueConstraints = @UniqueConstraint(name = "uk_matches_unique",
                columnNames = {"posting_id","disabled_user_id","caregiver_user_id"}))
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class Match {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(name = "posting_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long postingId;

    @Column(name = "disabled_user_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long disabledUserId;

    @Column(name = "caregiver_user_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long caregiverUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private MatchStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "decided_by", length = 16)
    private MatchDecidedBy decidedBy;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        var now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
        if (status == null) status = MatchStatus.PENDING;
    }
    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }
}
