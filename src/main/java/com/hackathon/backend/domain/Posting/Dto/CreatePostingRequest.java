package com.hackathon.backend.domain.Posting.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hackathon.backend.domain.Posting.Model.DayCode;
import com.hackathon.backend.domain.Posting.Model.TimeBand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreatePostingRequest(
        @NotBlank @Size(max=120) String title,
        @NotBlank @Size(max=50)  String periodStart,           // "6개월 이상"
        @NotEmpty List<@NotBlank String> preferredDays,        // ["월","수","금"]
        @NotEmpty List<@NotBlank String> timeBands,            // ["오전","심야"]
        @Size(max=5000) String description
) {}
