package com.hackathon.backend.domain.Posting.Dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class PostingDetailDto {
    private final Long postingId;
    private final Long userId;
    private final String title;
    private final String periodStart;
    private final String description;
    private final Set<Integer> preferredDays;
    private final Set<Integer> timeBands;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String region;
    private final Map<String, Object> classification; // ★ Map 타입

    public PostingDetailDto(
            Long postingId,
            Long userId,
            String title,
            String periodStart,
            String description,
            Set<Integer> preferredDays,
            Set<Integer> timeBands,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            String region,
            Map<String, Object> classification
    ) {
        this.postingId = postingId;
        this.userId = userId;
        this.title = title;
        this.periodStart = periodStart;
        this.description = description;
        this.preferredDays = preferredDays;
        this.timeBands = timeBands;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.region = region;
        this.classification = classification;
    }

    // Getter 전부 추가 (롬복 @Getter 써도 됨)
    public Long getPostingId() { return postingId; }
    public Long getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getPeriodStart() { return periodStart; }
    public String getDescription() { return description; }
    public Set<Integer> getPreferredDays() { return preferredDays; }
    public Set<Integer> getTimeBands() { return timeBands; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getRegion() { return region; }
    public Map<String, Object> getClassification() { return classification; }
}
