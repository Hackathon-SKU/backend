package com.hackathon.backend.domain.Posting.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(
        name = "postings_weekly_slots",
        indexes = {
                @Index(name = "idx_pws_posting", columnList = "posting_id"),
                @Index(name = "idx_pws_dow_time", columnList = "day_of_week,start_time,end_time")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class PostingWeeklySlots {

    /** 슬롯 ID (PK, BIGINT UNSIGNED AUTO_INCREMENT) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 부모 공고 FK */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "posting_id", nullable = false)
    @Setter // Posting.addSlot(...)에서 FK 세팅에 필요
    private Posting posting;

    /** 요일: 1=Mon … 7=Sun (TINYINT UNSIGNED) */
    @Column(name = "day_of_week", nullable = true)
    private Integer dayOfWeek;

    /** 시작 시각 (TIME) */
    @Column(name = "start_time", nullable = true)
    private LocalTime startTime;

    /** 종료 시각 (TIME) */
    @Column(name = "end_time", nullable = true)
    private LocalTime endTime;

    /** 생성/수정 시각 (DATETIME(6)) — JPA Auditing */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** 시간 구간 유효성 (start < end) — 필요 시 서비스에서 사용 */
    public boolean isValidRange() {
        return startTime == null || endTime == null || startTime.isBefore(endTime);
    }
}
