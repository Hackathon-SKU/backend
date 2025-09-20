package com.hackathon.backend.domain.Posting.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public record PostingDetailResponse(
        Long id,
        Long serviceUserId,
        String title,
        String periodStart,           // "6개월 이상" 같은 문자열
        List<String> preferredDays,   // ["월","수","금"]
        List<String> timeBands,       // ["오전","심야"]
        String description,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        String createdAt,     // 예: 2025-09-20T12:34:56.789Z

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        String updatedAt
) {}
