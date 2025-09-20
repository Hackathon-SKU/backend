package com.hackathon.backend.domain.Posting.Dto;

import com.hackathon.backend.domain.Posting.Entity.Posting;
import com.hackathon.backend.domain.Posting.Entity.PostingWeeklySlots;

import java.util.ArrayList;
import java.util.List;

public final class PostingMapper {
    private PostingMapper() {}

    /** 요청 DTO → 엔티티(슬롯 포함) */
    public static Posting toEntity(Long userId, CreatePostingRequest req) {
        Posting posting = Posting.builder()
                .userId(userId)
                .title(req.title())
                .periodStart(req.periodStart())
                .periodEnd(req.periodEnd())
                .description(req.description())
                .build();

        // weeklyRules를 요일별 단일 슬롯으로 확장하여 추가
        req.weeklyRules().forEach(rule ->
                rule.days().forEach(d ->
                        posting.addSlot(PostingWeeklySlots.builder()
                                .dayOfWeek(d)
                                .startTime(rule.startTime())
                                .endTime(rule.endTime())
                                .build())
                )
        );
        return posting;
    }

    /** (보조) 규칙 → 슬롯 리스트로만 뽑고 싶을 때 */
    public static List<PostingWeeklySlots> expandRulesToSlots(CreatePostingRequest req) {
        List<PostingWeeklySlots> slots = new ArrayList<>();
        req.weeklyRules().forEach(rule ->
                rule.days().forEach(d ->
                        slots.add(PostingWeeklySlots.builder()
                                .dayOfWeek(d)
                                .startTime(rule.startTime())
                                .endTime(rule.endTime())
                                .build())
                )
        );
        return slots;
    }

    /** 엔티티 → 응답 DTO */
    public static PostingResponse toResponse(Posting p) {
        var slotViews = p.getWeeklySlots().stream()
                .map(s -> new PostingResponse.WeeklySlotView(
                        s.getDayOfWeek(), s.getStartTime(), s.getEndTime()
                ))
                .toList();

        return new PostingResponse(
                p.getId(),
                p.getUserId(),
                p.getTitle(),
                p.getDescription(),
                p.getPeriodStart(),
                p.getPeriodEnd(),
                slotViews,
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
