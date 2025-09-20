package com.hackathon.backend.domain.Posting.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
        name = "postings",
        indexes = {
                @Index(name = "idx_postings_user_id", columnList = "user_id"),
                @Index(name = "idx_postings_period", columnList = "period_start,period_end")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", length = 120) // 필요시 120으로 늘리는 걸 권장
    private String title;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Lob
    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostingWeeklySlots> weeklySlots = new ArrayList<>();

    /** 편의 메서드 */
    public void addSlot(PostingWeeklySlots slot) {
        slot.setPosting(this);   // ← 자식 FK 세팅 (자식 엔티티에 @Setter 필요)
        weeklySlots.add(slot);
    }

    public void removeSlot(PostingWeeklySlots slot) {
        weeklySlots.remove(slot);
        slot.setPosting(null);
    }

    /** 기간 유효성 검사 */
    public boolean isValidPeriod() {
        return periodStart == null || periodEnd == null || !periodStart.isAfter(periodEnd);
    }

    /** 수정 */
    public void edit(String title, String description, LocalDate start, LocalDate end) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (start != null) this.periodStart = start;
        if (end != null) this.periodEnd = end;
    }
}
