package com.hackathon.backend.domain.Posting.Dto;

import java.util.Map;

public class PostingListItemDto {
    private final Long postingId;
    private final String title;
    private final Long userId;
    private final String region;
    private final Map<String, Object> classification;

    public PostingListItemDto(Long postingId, String title, Long userId,
                              String region, Map<String, Object> classification) {
        this.postingId = postingId;
        this.title = title;
        this.userId = userId;
        this.region = region;
        this.classification = classification;
    }

    public Long getPostingId() { return postingId; }
    public String getTitle() { return title; }
    public Long getUserId() { return userId; }
    public String getRegion() { return region; }
    public Map<String, Object> getClassification() { return classification; }
}
