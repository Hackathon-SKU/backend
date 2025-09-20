package com.hackathon.backend.domain.Posting.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreatePostingRequest(
        @NotBlank @Size(max = 120, message = "제목은 최대 120자입니다.")
        String title,

        @NotNull @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate periodStart,

        @NotNull @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate periodEnd,

        @Size(max = 5000)
        String description,

        @NotEmpty(message = "요일/시간 규칙은 1개 이상이어야 합니다.")
        List<@Valid WeeklyRule> weeklyRules
) {
    public record WeeklyRule(
            @NotEmpty(message = "days는 비어있을 수 없습니다.")
            List<@Min(value = 1, message = "요일은 1(월) 이상이어야 합니다.")
            @Max(value = 7, message = "요일은 7(일) 이하여야 합니다.") Integer> days,

            @NotNull @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,

            @NotNull @JsonFormat(pattern = "HH:mm")
            LocalTime endTime
    ) {}
}
