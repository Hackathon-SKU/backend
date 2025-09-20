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
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody CreatePostingRequest req
    ) {
        if (principal == null || principal.getUser() == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }
        Long userId = principal.getUser().getUserId();

        Long id = postingService.create(userId, req);

        // ✅ 생성 직후 상세 DTO 조회 (컬렉션까지 로딩된 메서드 사용)
        PostingDetailResponse detail = postingService.get(id);

        return ResponseEntity
                .created(URI.create("/api/postings/" + id))
                .body(GlobalWebResponse.success("OK", "성공", detail));
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