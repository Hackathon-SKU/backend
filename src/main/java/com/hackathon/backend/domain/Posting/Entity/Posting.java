package com.hackathon.backend.domain.Posting.Entity;

import com.hackathon.backend.domain.Posting.Model.DayCode;
import com.hackathon.backend.domain.Posting.Model.TimeBand;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
// 다른 import들...
import org.springframework.security.core.annotation.AuthenticationPrincipal;  // ★ 이거 추가


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "postings")
public class Posting {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable=false, length=120)
    private String title;

    // 기간을 날짜로 쓸 거면 LocalDate, "6개월 이상"처럼 텍스트면 String으로
    @Column(name="period_start_text", length=50)
    private String periodStart;   // 예: "6개월 이상"

    @Lob
    private String description;

    /** 희망 요일: 1=월 … 7=일 */
    @ElementCollection
    @CollectionTable(name="posting_preferred_days", joinColumns=@JoinColumn(name="posting_id"))
    @Column(name="day_code", nullable=false) // INT
    @Builder.Default
    private Set<Integer> preferredDays = new LinkedHashSet<>();

    /** 희망 시간대: 1=오전, 2=오후, 3=심야 */
    @ElementCollection
    @CollectionTable(name="posting_time_bands", joinColumns=@JoinColumn(name="posting_id"))
    @Column(name="time_band_code", nullable=false) // INT
    @Builder.Default
    private Set<Integer> timeBands = new LinkedHashSet<>();

    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp   private LocalDateTime updatedAt;

    // 편의 수정 메서드 등은 필요시 추가
}

