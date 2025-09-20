package com.hackathon.backend.domain.Posting.Service;

import com.hackathon.backend.domain.Posting.Dto.CreatePostingRequest;
import com.hackathon.backend.domain.Posting.Dto.PostingMapper;
import com.hackathon.backend.domain.Posting.Dto.PostingResponse;
import com.hackathon.backend.domain.Posting.Entity.Posting;
import com.hackathon.backend.domain.Posting.Repository.PostingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;

    /** 공고 생성 */
    @Transactional
    public Long create(Long userId, CreatePostingRequest req) {
        validateRequest(req);
        Posting posting = PostingMapper.toEntity(userId, req); // 슬롯까지 담긴 엔티티 생성
        return postingRepository.save(posting).getId();
    }

    /** 단건 조회 (응답 DTO 변환) */
    @Transactional(Transactional.TxType.SUPPORTS)
    public PostingResponse get(Long id) {
        Posting p = postingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 공고가 존재하지 않습니다. id=" + id));
        return PostingMapper.toResponse(p);
    }

    /* ================== 내부 검증 로직 ================== */

    private void validateRequest(CreatePostingRequest req) {
        // 1) 기간 유효성: start <= end
        if (req.periodStart().isAfter(req.periodEnd())) {
            throw new IllegalArgumentException("periodStart는 periodEnd보다 이후일 수 없습니다.");
        }

        // 2) 요일/시간대 유효성 및 겹침 금지
        //    - days: 1..7
        //    - startTime < endTime
        //    - 같은 요일 내 구간 겹침 금지
        Map<Integer, List<CreatePostingRequest.WeeklyRule>> byDow = new HashMap<>();
        for (CreatePostingRequest.WeeklyRule rule : req.weeklyRules()) {
            if (!rule.startTime().isBefore(rule.endTime())) {
                throw new IllegalArgumentException("시작 시간이 종료 시간보다 같거나 늦을 수 없습니다. (" +
                        rule.startTime() + "~" + rule.endTime() + ")");
            }
            for (Integer d : rule.days()) {
                if (d == null || d < 1 || d > 7) {
                    throw new IllegalArgumentException("요일 값은 1(월)~7(일)이어야 합니다. 값=" + d);
                }
                byDow.computeIfAbsent(d, k -> new ArrayList<>()).add(rule);
            }
        }

        // 같은 요일 내 겹침 검사
        for (Map.Entry<Integer, List<CreatePostingRequest.WeeklyRule>> e : byDow.entrySet()) {
            int dow = e.getKey();
            // 규칙을 슬롯(단일 구간) 리스트로 전개
            List<TimeRange> ranges = e.getValue().stream()
                    .map(r -> new TimeRange(r.startTime(), r.endTime()))
                    .sorted(Comparator.comparing(TimeRange::start))
                    .collect(Collectors.toList());

            for (int i = 1; i < ranges.size(); i++) {
                LocalTime prevEnd = ranges.get(i - 1).end();
                LocalTime currStart = ranges.get(i).start();
                if (!prevEnd.isBefore(currStart)) {
                    throw new IllegalArgumentException("요일 " + dow + "에서 시간 구간이 겹칩니다. " +
                            ranges.get(i - 1) + " vs " + ranges.get(i));
                }
            }
        }
    }

    /* 간단한 시간 구간용 레코드 */
    private record TimeRange(LocalTime start, LocalTime end) {
        @Override public String toString() { return start + "~" + end; }
    }
}
