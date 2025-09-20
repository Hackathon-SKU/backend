package com.hackathon.backend.domain.Posting.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record PostingResponse(
        Long id,
        Long serviceUserId,
        String title,
        String description,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate periodStart,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate periodEnd,

        List<WeeklySlotView> weeklySlots,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime updatedAt
) {
    public record WeeklySlotView(
            Integer dayOfWeek,
            @JsonFormat(pattern = "HH:mm") LocalTime startTime,
            @JsonFormat(pattern = "HH:mm") LocalTime endTime
    ) {}
}
