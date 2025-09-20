package com.hackathon.backend.domain.Posting.Dto;

import com.hackathon.backend.domain.Posting.Entity.Posting;
import com.hackathon.backend.domain.Posting.support.SimpleDayTimeMapper;

import java.time.ZoneOffset;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class PostingMapper {
    private PostingMapper() {}

    // Create 요청 → 엔티티 (userId 받도록 수정)
    public static Posting toEntity(Long userId, CreatePostingRequest req) {
        Set<Integer> dayCodes = req.preferredDays().stream()
                .map(SimpleDayTimeMapper::dayKoToCode)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<Integer> bandCodes = req.timeBands().stream()
                .map(SimpleDayTimeMapper::bandKoToCode)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Posting.builder()
                .userId(userId)                 // ← 이제 컴파일됨
                .title(req.title())
                .periodStart(req.periodStart()) // 문자열이면 String, 날짜면 LocalDate로 맞추세요
                .description(req.description())
                .preferredDays(dayCodes)
                .timeBands(bandCodes)
                .build();
    }

    // 엔티티 → 응답
    public static PostingDetailResponse toResponse(Posting p) {
        List<String> daysKo = p.getPreferredDays().stream()
                .map(SimpleDayTimeMapper::dayCodeToKo)
                .toList();

        List<String> bandsKo = p.getTimeBands().stream()
                .map(SimpleDayTimeMapper::bandCodeToKo)
                .toList();

        var createdZ = p.getCreatedAt() == null ? null : p.getCreatedAt().atOffset(ZoneOffset.UTC).toString();
        var updatedZ = p.getUpdatedAt() == null ? null : p.getUpdatedAt().atOffset(ZoneOffset.UTC).toString();

        return new PostingDetailResponse(
                p.getId(),
                p.getUserId(),
                p.getTitle(),
                p.getPeriodStart(),
                daysKo,
                bandsKo,
                p.getDescription(),
                createdZ,
                updatedZ
        );
    }
}