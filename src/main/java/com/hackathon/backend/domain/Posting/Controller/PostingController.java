package com.hackathon.backend.domain.Posting.Controller;

import com.hackathon.backend.domain.Posting.Dto.CreatePostingRequest;
import com.hackathon.backend.domain.Posting.Dto.PostingDetailResponse;
import com.hackathon.backend.domain.Posting.Service.PostingService;
import com.hackathon.backend.global.Response.GlobalWebResponse;
import com.hackathon.backend.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/postings")
@RequiredArgsConstructor
@Validated
public class PostingController {

    private final PostingService postingService;

    /** 공고 생성 (프론트는 한국어로 전달: preferredDays=["월","수",...], timeBands=["오전","심야"]) */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<GlobalWebResponse<?>> create(
            @AuthenticationPrincipal CustomUserDetails principal,   // ★ 추가
            @Valid @RequestBody CreatePostingRequest req
    ) {
        if (principal == null || principal.getUser() == null) {
            // 인증 필수라면 401/403으로 처리(전역 핸들러로 보내도 됨)
            throw new IllegalStateException("인증 정보가 없습니다.");
        }
        Long userId = principal.getUser().getUserId();             // ★ 여기서 id 가져옴

        Long id = postingService.create(userId, req);              // ★ 서비스에 전달

        return ResponseEntity
                .created(URI.create("/api/postings/" + id))
                .body(GlobalWebResponse.success("OK", "성공", Map.of("id", id)));
    }


    /** 공고 단건 조회 (응답은 한국어로 변환된 값) */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<GlobalWebResponse<?>> get(@PathVariable Long id) {
        PostingDetailResponse data = postingService.get(id);
        return ResponseEntity.ok(
                GlobalWebResponse.success("OK", "성공", data)
        );
    }

    /** 공고 목록 조회 (페이징) */
    @GetMapping(produces = "application/json")
    public ResponseEntity<GlobalWebResponse<?>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,DESC") String sort // 예: "createdAt,DESC"
    ) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<PostingDetailResponse> resPage = postingService.list(pageable);

        Map<String, Object> payload = Map.of(
                "postings", resPage.getContent(),          // 한국어 변환된 리스트
                "page", resPage.getNumber(),
                "size", resPage.getSize(),
                "totalElements", resPage.getTotalElements(),
                "totalPages", resPage.getTotalPages(),
                "hasNext", resPage.hasNext()
        );

        return ResponseEntity.ok(
                GlobalWebResponse.success("OK", "성공", payload)
        );
    }


    /** 공고 삭제 */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<GlobalWebResponse<?>> delete(@PathVariable Long id) {
        postingService.delete(id);
        return ResponseEntity.ok(
                GlobalWebResponse.success("OK", "성공", Map.of("id", id))
        );
    }

    /* ===== 내부 유틸 ===== */
    private Sort parseSort(String input) {
        // "field,DESC" 또는 "field,ASC"
        String[] parts = input.split(",");
        String field = parts[0];
        Sort.Direction dir = parts.length > 1 ? Sort.Direction.fromString(parts[1]) : Sort.Direction.DESC;
        return Sort.by(dir, field);
    }
}
