// src/main/java/com/hackathon/backend/domain/Posting/Dto/PostingDetailResponse.java
package com.hackathon.backend.domain.Posting.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.List;

public record PostingResponse(
        Long id,
        String title,
        String periodStart,
        List<String> preferredDays,    // ["월","수","금"]
        List<String> timeBands,        // ["오후","심야"]
        String description,
        String createdAt,              // "2025-09-20T12:34:56.789Z"
        String updatedAt
) {}
